from impala.dbapi import connect


def go():
    conn = connect(host='emr-header-1.cluster-89237', port=21050)
    cursor = conn.cursor()
    print(cursor)

    cur = cursor
    cur_execute = cur.execute('SHOW TABLES')
    print(cur_execute)

    execute = cur.execute('SELECT * FROM tmp.port limit 1')
    print(execute)

    data = cur.fetchall()
    print (data)
    print (type(data))

if __name__ == '__main__':
    go()
