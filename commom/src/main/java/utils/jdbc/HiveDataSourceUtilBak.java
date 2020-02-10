package utils.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 *
 */
public class HiveDataSourceUtilBak {
    private static DruidDataSource hiveDataSource = new DruidDataSource();
    public static Connection conn = null;
    private static final Logger log = LoggerFactory.getLogger(HiveDataSourceUtilBak.class);

    public static DruidDataSource getHiveDataSource() {
        if (hiveDataSource.isInited()) {
            return hiveDataSource;
        }

        try {

            Properties dsProp = new Properties();
            InputStream inputStream = HiveDataSourceUtilBak.class.getClassLoader().getResourceAsStream("hive_druid.properties");
            dsProp.load(inputStream);
            //Properties dsProp = PropertiesUtil.getDataSourceProp();
            //基本属性 url、user、password
            hiveDataSource.setDriverClassName(dsProp.getProperty("hive.driverClassName"));
            hiveDataSource.setUrl(dsProp.getProperty("hive.url"));
            hiveDataSource.setUsername(dsProp.getProperty("hive.username"));
            hiveDataSource.setPassword(dsProp.getProperty("hive.password"));
            //hiveDataSource.setPassword("000000");

            //配置初始化大小、最小、最大
            hiveDataSource.setInitialSize(Integer.parseInt(dsProp.getProperty("hive.initialSize")));
            hiveDataSource.setMinIdle(Integer.parseInt(dsProp.getProperty("hive.minIdle")));
            hiveDataSource.setMaxActive(Integer.parseInt(dsProp.getProperty("hive.maxActive")));

            //配置获取连接等待超时的时间
            hiveDataSource.setMaxWait(Integer.parseInt(dsProp.getProperty("hive.maxWait")));

            //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
            hiveDataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(dsProp.getProperty("hive.timeBetweenEvictionRunsMillis")));

            //配置一个连接在池中最小生存的时间，单位是毫秒
            hiveDataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(dsProp.getProperty("hive.minEvictableIdleTimeMillis")));

            hiveDataSource.setValidationQuery(dsProp.getProperty("hive.validationQuery"));
            hiveDataSource.setTestWhileIdle(Boolean.valueOf(dsProp.getProperty("hive.testWhileIdle")));
            hiveDataSource.setTestOnBorrow(Boolean.valueOf("hive.testOnBorrow"));
            hiveDataSource.setTestOnReturn(Boolean.valueOf("hive.testOnReturn"));

            //打开PSCache，并且指定每个连接上PSCache的大小
            hiveDataSource.setPoolPreparedStatements(Boolean.valueOf(dsProp.getProperty("hive.poolPreparedStatements")));
            hiveDataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.valueOf(dsProp.getProperty("hive.maxPoolPreparedStatementPerConnectionSize")));

            //配置监控统计拦截的filters
           hiveDataSource.setFilters(dsProp.getProperty("hive.filters"));

            // 初始化连接池
            hiveDataSource.init();
        } catch (Exception e) {
            e.printStackTrace();
            closeHiveDataSource();
        }
        return hiveDataSource;
    }

    /**
     * @Description:关闭Hive连接池
     */
    public static void closeHiveDataSource() {
        if (hiveDataSource != null) {
            hiveDataSource.close();
        }
    }

    /**
     * @return
     * @Description:获取Hive连接
     */
    public static Connection getHiveConn() {
        try {
            hiveDataSource = getHiveDataSource();
            conn = hiveDataSource.getConnection();
        } catch (SQLException e) {
            log.error("--" + e + ":获取Hive连接失败！");
        }
        return conn;
    }

    /**
     * @Description:关闭Hive数据连接
     */
    public static void closeConn() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            log.error("--" + e + ":关闭Hive-conn连接失败！");
        }
    }

    public static void close(ResultSet resultSet,Statement statement,Connection connection){
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DataSource ds = HiveDataSourceUtilBak.getHiveDataSource();
        Connection conn = ds.getConnection();
        Statement stmt = null;
        ResultSet res = null;
        if (conn == null) {
            System.out.println("null");
            log.error("connection is null");
        } else {
            System.out.println("connnction seccess");
            stmt = conn.createStatement();
            res = stmt.executeQuery("select * from test_date_text");
            int i = 0;
            while (res.next()) {
                if (i < 10) {
                    System.out.println(res.getString(1));
                    System.out.println(hiveDataSource.getMaxActive());
                    System.out.println(hiveDataSource.getMinIdle());
                    i++;
                }
            }
        }
        HiveDataSourceUtilBak.close(res,stmt,conn);
    }
}