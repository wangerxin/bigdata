# -*- coding: UTF-8 -*-
import smtplib
import traceback
import subprocess
from datetime import datetime,timedelta
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart


def sendmail(subject, msg, toaddrs, fromaddr, smtpaddr, password):
    '''
    @subject:邮件主题
    @msg:邮件内容
    @toaddrs:收信人的邮箱地址
    @fromaddr:发信人的邮箱地址
    @smtpaddr:smtp服务地址，可以在邮箱看，比如163邮箱为smtp.163.com
    @password:发信人的邮箱密码
    '''
    mail_msg = MIMEMultipart()
    #if not isinstance(subject, unicode):
    #    subject = unicode(subject, 'utf-8')
    mail_msg['Subject'] = subject
    mail_msg['From'] = fromaddr
    mail_msg['To'] = ','.join(toaddrs)
    mail_msg.attach(MIMEText(msg, 'html', 'utf-8'))
    try:
        s = smtplib.SMTP_SSL()
        s.connect(smtpaddr, 465)  # 连接smtp服务器
        s.login(fromaddr, password)  # 登录邮箱
        s.sendmail(fromaddr, toaddrs, mail_msg.as_string())  # 发送邮件
        s.quit()
    except Exception as e:
        print ("Error: unable to send email")
        print (traceback.format_exc())


if __name__ == '__main__':
    fromaddr = "wangerxin@3commas.cn"
    smtpaddr = "smtp.mxhichina.com"
    toaddrs = ["wangerxin@3commas.cn"]
    subject = "主题"
    password = "3commas@666"
    msg = "正文"

    #日期
    start_date = datetime.strftime(datetime.now() - timedelta(9) ,'%Y-%m-%d')
    end_date = datetime.strftime(datetime.now() - timedelta(3) ,'%Y-%m-%d')

    lingchang_lengqi_sql = '''
    select 
        stat_date 日期,
        voice_publish 发布音频数,
        voice_success 音频冷起数,
        voice_success_rate 音频冷起比率,
        video_publish 发布视频数,
        video_success 视频冷起数,
        video_success_rate  视频冷起比率 
    from
        app.lingchang_lengqi
    where 
        stat_date >= '{}' and stat_date <= '{}'
    order by stat_date;
    '''.format(start_date,end_date)
    print(lingchang_lengqi_sql)

    qiang_jie_lengqi_sql = '''
    Select 
        stat_date 日期,
        game_type 抢接,
        title  曲库,
        scid_count 题卡总数,
        success_count 题卡冷起数,
        success_rate  题卡冷起比率
    from 
        app.qiangchang_jiechang_lengqi
    where
        stat_date >= '{}' and stat_date <= '{}'
    order by
        stat_date,game_type,success_rate desc;
    '''.format(start_date,end_date)
    print(qiang_jie_lengqi_sql)

    list = []
    list.append("a")
    print(list)

    #执行cmd


    #发送邮件
    #sendmail(subject, msg, toaddrs, fromaddr, smtpaddr, password)
