import pandas as pd
def convertToHtml(result,title):
    #将数据转换为html的table
    #result是list[list1,list2]这样的结构
    #title是list结构；和result一一对应。titleList[0]对应resultList[0]这样的一条数据对应html表格中的一列
    d = {}
    index = 0
    for t in title:
        d[t]=result[index]
        index = index+1
    df = pd.DataFrame(d)
    df = df[title]
    h = df.to_html(index=False)
    return h

if __name__ == '__main__':
    result = [[u'2016-08-25',u'2016-08-26',u'2016-08-27'],[u'张三',u'李四',u'王二']]
    title = [u'日期',u'姓名']
    print(convertToHtml(result,title))