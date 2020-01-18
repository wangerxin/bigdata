import pandas as pd

list1 = [[1,2,3],[3,4,5]]
df1 = pd.DataFrame(list1,columns=["a","b","c"])

# apply,理解为spark的map函数
"""
axis: function作用于行还是列
      0: 列
      1: 行
"""
def func(row):
    print(row)
    print(row["a"])

# 方式一: lambda
#df1.apply(lambda row: func(row))

# 方式二: 函数名
df1.apply(func,axis=1)

