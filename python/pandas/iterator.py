# https://www.jb51.net/article/172623.htm
# iterrows(): 按行遍历，将DataFrame的每一行迭代为(index, Series)对，可以通过row[name]对元素进行访问。
# itertuples(): 按行遍历，将DataFrame的每一行迭代为元祖，可以通过row[name]对元素进行访问，比iterrows()效率高。
# iteritems():按列遍历，将DataFrame的每一列迭代为(列名, Series)对，可以通过row[index]对元素进行访问。

import pandas as pd
list1 = [[1,2,3],[3,4,5]]
df1 = pd.DataFrame(list1,columns=["a","b","c"])

# iterrows(): 按照行遍历
# for index,row in df1.iterrows():
#     print(index)
#     print(row)
#     print(row[0])
#     print(row["a"])

# 按照行遍历
# for row in df1.itertuples():
#     print(row[0])
#     print(getattr(row, "a"))

# 按照列遍历
for column_name,column_value in df1.iteritems():
    print(column_name)
    print(column_value)
    print(column_value[0])
