# coding=utf-8
'''
专场与题卡的统计（过滤黑名单）
'''
import sys
import os
curPath = os.path.abspath(os.path.dirname(__file__))
rootPath = os.path.split(curPath)[0]
sys.path.append(rootPath)
import pymysql
from datetime import date, timedelta
from common import MysqldbHelper
from config import redis_config
import redis
import subprocess

cur = None
game_theme_id = 'cards_of_blacklist::'
blacklist_dict = {}

#load 有分区表
LOAD_DATA_PART = "ALTER TABLE dws.s_card_special_statistics_i ADD IF NOT EXISTS PARTITION (stat_date='{partition}') location '/user/hive/warehouse/dws.db/s_card_special_statistics_i/stat_date={partition}'"
#上传文件到hdfs
HDFS_LOAD_DATA = "hdfs dfs -put {} /user/hive/warehouse/dws.db/s_card_special_statistics_i/stat_date={}"

def init_redis():
    global redis_client
    redis_client = redis.StrictRedis(host=redis_config.config.REDIS_HOST, db=redis_config.config.REDIS_DB, port=redis_config.config.REDIS_PORT, password=redis_config.config.REDIS_PASSWORD, decode_responses=True)

def init_mysql_connect():
    global cur
    conn = pymysql.connect(host='10.110.83.100', port=3336, user='jianglibo', password='PtVvV4LZ6EjS7PToUsLe', db='qcard_inyuapp')
    cur = conn.cursor()

#在mysql获取game_theme_id 的黑名单（和cold_start不同）
def get_blacklist():
    global blacklist_dict
    cur.execute("select sid from special where status=1 ")
    results = cur.fetchall()
    for result in results:
        # game_theme_id  card_id
        blacklist_dict[result[0]] = redis_client.smembers(game_theme_id+str(result[0]))


def card_special_statistics(task_day,f):
    try:
        mydb = MysqldbHelper.MysqldbHelper()
        tablename = 'card_special_statistics'
        params = {}
        params["base_date"] = task_day
        mydb.delete(tablename, params)

        #scid:题卡ID  sid:专场ID
        cur.execute("select t1.sid,t1.scid from (select sid,scid from card_special where status=1) t1 join (select scid from song_cards where status=1) t2  on t1.scid=t2.scid")

        card_special_results = cur.fetchall()
        #sid:专场ID title:专场名称 type:抢唱 9050 ;接唱 9051
        cur.execute("select sid,type,title from special where status=1")
        special_results = cur.fetchall()

        for special_result in special_results:
            num = 0
            for card_special_result in card_special_results:
                if(card_special_result[0]==special_result[0] and (not str(card_special_result[1]) in blacklist_dict[card_special_result[0]])):
                    num = num + 1
            params["game_theme_id"] = str(special_result[0])
            params["game_type"] = str(special_result[1])
            params["title"] = special_result[2]
            params["num"] = str(num)
            mydb.insert(tablename, params)
            s = '{}\t{}\t{}\t{}\n'.format(special_result[0], special_result[1], special_result[2], num)
            f.write(s)
        f.close
    except Exception as e:
        print(e)
        sys.exit(1)

if __name__ == "__main__":
    # mysql初始化
    init_mysql_connect()
    # redis初始化
    init_redis()
    # 获取game_theme_id对应的黑名单
    get_blacklist()
    #专场和题卡的统计
    task_day = (date.today() + timedelta(days=-1)).strftime("%Y-%m-%d")

    file_name = '/tmp/card_special_statistics/card_special_statistics_{}.txt'.format(task_day)
    with open(file_name, 'w') as f:
        card_special_statistics(task_day,f)
    load_sql = LOAD_DATA_PART.format(partition=task_day)
    print(load_sql)
    result = subprocess.call(["hive", "-e", load_sql], shell=False)
    print(result)
    if result == 0:
        hdfs_load = HDFS_LOAD_DATA.format(file_name, task_day)
        print(hdfs_load)
        subprocess.call(hdfs_load, shell=True)