# -*- coding: utf-8 -*-
#!/usr/bin/env python
# @Time    : 2017/12/22 17:50
# @Desc    :
# @File    : mailtools.py
# @Software: PyCharm
import smtplib,time
from email.mime.text import MIMEText

#使用的邮箱的smtp服务器地址，这里是163的smtp地址
mail_host="smtp.qq.com"
#qq邮箱第三方登录密码
qq_smtp="dqcftjvmatnmbfgb"

# 用户名
mail_user="997520707"
#密码
mail_pass="tx235236"
#邮箱的后缀，网易就是163.com
mail_postfix="qq.com"


def send_mail(to_list,sub,content):

    print("开始发邮件")
    me="<"+mail_user+"@"+mail_postfix+">"
    msg = MIMEText(content, _subtype='plain', _charset='utf-8')
    msg['Subject'] = sub
    msg['From'] = me
    msg['To'] = ";".join(to_list)                #将收件人列表以‘；’分隔
    try:
        server = smtplib.SMTP()
        # 连接服务器
        print("开始连接服务器")
        server.connect(mail_host)
        print("连接服务器成功")
        # 登录操作
        server.login(mail_user,qq_smtp)
        server.sendmail(me, to_list, msg.as_string())
        server.close()
        print("结束发邮件")
        return True
    except Exception as e:
        print(e)
        return False


'''
定时函数默认60秒
'''
def re_exe(run_time,inc = 60):
    while True:
        # 邮件主题和邮件内容
        # 这是最好写点中文，如果随便写，可能会被网易当做垃圾邮件退信
        if  send_mail(['997520707@qq.com'],"主题","正文"):
            print ("success!")
        else:
            print ("failed!")
        time.sleep(inc)

if __name__ == '__main__':
    # 发送1封，上面的列表是几个人，这个就填几
    for i in range(1):
        #输入定时时间
        re_exe("echo %time%", 10)