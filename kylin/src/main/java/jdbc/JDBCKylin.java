package jdbc;

import java.sql.*;

public class JDBCKylin {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //1.连接kylin
        String KYLIN_DRIVER = "org.apache.kylin.jdbc.Driver";

        //Kylin_URL
        String KYLIN_URL = "jdbc:kylin://hadoop102:7070/first";

        //Kylin的用户名
        String KYLIN_USER = "ADMIN";

        //Kylin的密码
        String KYLIN_PASSWD = "KYLIN";

        //添加驱动信息
        Class.forName(KYLIN_DRIVER);

        //连接kylin
        Connection connection = DriverManager.getConnection(KYLIN_URL, KYLIN_USER, KYLIN_PASSWD);

        //2.预编译sql
        String sql="SELECT sum(sal) FROM emp group by deptno;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //3.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();

        //4.分析sql结果
        while (resultSet.next()){
            int sumSal = resultSet.getInt(1);
            System.out.println(sumSal);
        }

    }
}
