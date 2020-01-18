import pandas as pd
# https://www.jianshu.com/p/b50941b6d229

list1 = [[1,2,3],[1,4,5],[2,2,3]]
df1 = pd.DataFrame(list1,columns=["a","b","c"])
#    a  b  c
# 0  1  2  3
# 1  1  4  5
# 2  2  2  3

# 1.gruopby: 划分为若干个DataFrame,groupby之后数据条数并没有减少,知识分组了
# df = df1.groupby("a")
# [(1,
#   a  b  c
# 0  1  2  3
# 1  1  4  5),
#  (2,
#    a  b  c
# 2  2  2  3)]

# 2.groupby之后可以进行agg聚合操作,数据量会减少,类似hql中的groupby
# 对每一列都进行聚合
# df = df1.groupby("a").agg("max")
# 对b这一列进行聚合
# df = df1.groupby("a")["b"].max()
# 对不同的列做不同的聚合
# df = df1.groupby("a").agg({"b":"max","c":"min"})


# 3.groupby之后可以进行transform操作,把结果作用在新的一列,数据条数不变
df1["d"] = df1.groupby("a")["b"].transform("max")
print(df1)



