import pandas as pd

list1 = [[1,2,3],[1,4,5],[2,2,3]]
df1 = pd.DataFrame(list1,columns=["a","b","c"])

# 透视表与分组表的效果是一样的
pd.pivot(df1,columns="a",values="b")

# cut
# 将数据划分为某一个分类,例如5岁放在0-9岁