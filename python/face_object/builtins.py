# 1.利用反射,获取成员,设置成员值
# https://blog.csdn.net/weixin_43567965/article/details/86516651
class A:
    pass

a = A
setattr(a,"name","zhangsan")
getattr(a, "name")

