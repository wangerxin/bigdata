import pandas as pd
# 请参见博客 https://www.cnblogs.com/guxh/p/9451532.html

#实现
list1 = [[1,2,3],[3,4,5]]
df1 = pd.DataFrame(list1,columns=["a","b","c"])

list2 = [[3,12,13],[13,14,15]]
df2 = pd.DataFrame(list2,columns=["c","e","d"])

# 1.pd.concat([df1,df2...]): 合并两个或者多个df
"""
axis: 合并方式
      0:先添加行,然后对齐相同列,默认值.
      1:先添加列,然后对齐相同行
      
join: 合并之后,取所有的行/列,还是取相同的行/列
      outer: 取所有的,默认值.
      inner: 取相同的
      
join_axes : 合并之后取哪些行列
            None: 取所有列
            df1.index: 取部分行
            df2.columns: 取部分列
            
ignore_index: 是否忽略原来的索引
              false: 默认值
              true: 忽略原来的索引,重建索引,从0开始
              
keys: 合并之后,可以加一层标签，标识行/列名称属于原来哪个df.
      None: 默认值,不加标签
      
sort: 非合并方向的行/列名称是否排序
       false: 默认值
      
"""
df = pd.concat([df1,df2],axis=0)
# 等价于df1.append(df2)
# a    b   c     d     e
# 0  1.0  2.0   3   NaN   NaN
# 1  3.0  4.0   5   NaN   NaN
# 0  NaN  NaN  11  12.0  13.0
# 1  NaN  NaN  13  14.0  15.0

df = pd.concat([df1,df2],axis=1)
# a  b  c   c   d   e
# 0  1  2  3  11  12  13
# 1  3  4  5  13  14  15

df = pd.concat([df1,df2],axis=0,join="inner")
# c
# 0   3
# 1   5
# 0  11
# 1  13

df = pd.concat([df1,df2],join_axes=[df2.columns])
# c     d     e
# 0   3   NaN   NaN
# 1   5   NaN   NaN
# 0  11  12.0  13.0
# 1  13  14.0  15.0

# 2.pd.merge([df1,df2]): 根据columns join,类似于sql的join
"""
how: 连接方式,与sql是一样的
     outer: 全连接,默认值
     inner:
     left:
     right
"""
df = pd.merge(df1,df2,how="left")

# 3.
df = df1.join(df2)
print(df)
