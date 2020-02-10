package utils.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mysql.jdbc.StringUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 使用druid连接池,本案例做了mysql和hive的连接池测试
 */
public class DataSourceUtil {

    // 用DataSource即可
    private static DruidDataSource dataSource ;

    static{
        try {
            //1.加载配置文件
            Properties pro = new Properties();
            pro.load(DataSourceUtil.class.getClassLoader().getResourceAsStream("hive_druid.properties"));
            //2.创建连接池
            dataSource = (DruidDataSource)DruidDataSourceFactory.createDataSource(pro);
            //第二种配置方式dataSource.setUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接池方法
     */
    public static DataSource getDataSource(){
        return  dataSource;
    }

    public static void colseDataSource(){
        dataSource.close();
    }

    /**
     * 获取连接
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 关闭资源
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {

        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(connection != null){
            try {

                //如果采用的是连接池,close方法实际被增强了,他并不是关闭连接,而是归还连接到连接池
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取所有表名和列名
     * @param conStr 支持jdbc协议,mysql,hive...
     * @param user
     * @param password
     * @return
     */
    public static Map<String, Map<String, String>> getTableNameAndColumn(String conStr, String user, String password) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet tableSet = null;
        ResultSet columnResultSet = null;
        Map<String, Map<String, String>> tableColumnsMap = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(conStr, user, password);

            //获取所有表的元数据
            DatabaseMetaData metaData = con.getMetaData();
            tableSet = metaData.getTables(null, null, "%", null);

            //获取每个表的所有列
            tableColumnsMap = new HashMap<>();
            while (tableSet.next()) {
                Map<String, String> columnTypeMap = new HashMap<>();
                String tableName = tableSet.getString("TABLE_NAME");
                if (!StringUtils.isNullOrEmpty(tableName)) {
                    ps = con.prepareStatement("select * from " + tableName + " limit 1");
                    ResultSetMetaData tableMetaData = ps.getMetaData();
                    int columnCount = tableMetaData.getColumnCount();
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = tableMetaData.getColumnName(i + 1);
                        String columnTypeName = tableMetaData.getColumnTypeName(i + 1);
                        if (!StringUtils.isNullOrEmpty(columnName)) {
                            columnTypeMap.put(columnName, columnTypeName);
                        }
                    }
                }
                tableColumnsMap.put(tableName, columnTypeMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataSourceUtil.close(tableSet, ps, con);
            return tableColumnsMap;
        }
    }

    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from test_date_text");
            if (resultSet.next()){
                String data = resultSet.getString(1);
                System.out.println(data);
            }
            System.out.println("size: " + dataSource.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataSourceUtil.close(resultSet,statement,connection);
            DataSourceUtil.colseDataSource();
        }

    }


}
