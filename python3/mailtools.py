# -*- coding: utf-8 -*-
#!/usr/bin/env python
# @Time    : 2017/12/22 17:50
# @Desc    :
# @File    : mailtools.py
# @Software: PyCharm
import smtplib,time
from email.mime.text import MIMEText
#使用的邮箱的smtp服务器地址，这里是163的smtp地址
mail_host="smtp.163.com"
# 用户名
mail_user="***"
#密码
mail_pass="***"
#邮箱的后缀，网易就是163.com
mail_postfix="163.com"


def send_mail(to_list,sub,content):
    me="<"+mail_user+"@"+mail_postfix+">"
    msg = MIMEText(content, _subtype='plain', _charset='utf-8')
    msg['Subject'] = sub
    msg['From'] = me
    msg['To'] = ";".join(to_list)                #将收件人列表以‘；’分隔
    try:
        server = smtplib.SMTP()
        # 连接服务器
        server.connect(mail_host)
        # 登录操作
        server.login(mail_user,mail_pass)
        server.sendmail(me, to_list, msg.as_string())
        server.close()
        return True
    except Exception :
        return False


'''
定时函数默认60秒
'''
def re_exe(inc = 60):
    while True:
        # 邮件主题和邮件内容
        # 这是最好写点中文，如果随便写，可能会被网易当做垃圾邮件退信
        if  send_mail(['******@qq.com'],"删除","请按删除键"):
            print ("done!")
        else:
            print ("failed!")
        time.sleep(inc)

if __name__ == '__main__':
    # 发送1封，上面的列表是几个人，这个就填几
    for i in range(100):
        #输入定时时间
        re_exe("echo %time%", 60)