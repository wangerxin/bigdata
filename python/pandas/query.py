import pandas as pd
# 创建
pd1 = pd.DataFrame([[1,2,3],[4,5,6],[7,8,9]])

# 查看属性(为什么没有提示)
print(pd1.index)
print(pd1.columns)
print(pd1.values)

# 索引
# 单行
#print(pd1.loc[0, :])
# 多行
# print(pd1.loc[0:1,:]) # 连续多行,包头包尾
# print(pd1.loc[[0,1],:]) # 不连续多行,包头包尾
# 单列
# print(pd1.loc[:, 0])
# 多列
# print(pd1.loc[:, 0:1]) # 连续多行,包头包尾
# print(pd1.loc[:,[0, 1]]) # 连续多行,包头包尾
# 单个数据
# print(pd1.at[0, 0])

