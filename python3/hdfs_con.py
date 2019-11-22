import sys
from hdfs.client import Client

#设置utf-8模式
#reload(sys)
#sys.setdefaultencoding( "utf-8" )


# 关于python操作hdfs的API可以查看官网:
# https://hdfscli.readthedocs.io/en/latest/api.html

# 创建文件
def create_file(client, file_path):
    pass


# 追加数据到hdfs文件
def append_to_hdfs(client, hdfs_path, data):
    client.write(hdfs_path, data, overwrite=False, append=True, encoding='utf-8')


# 覆盖数据写到hdfs文件
def write_to_hdfs(client, hdfs_path, data):
    client.write(hdfs_path, data, overwrite=True, append=False, encoding='utf-8')




# 往hdfs文件写入数据
def go():
    list = {1, 2, 3, 4, 5}
    client = Client("http://10.110.83.17:50070/", root="/", timeout=10000, session=False)
    for i in list:
        pass
        #append_to_hdfs(client, '/3commas/data/test/hdfs_con.txt', str(i)+"\n")
    client_list = client.list("/")
    for name in client_list:
        print(name)


if __name__ == '__main__':
    go()
