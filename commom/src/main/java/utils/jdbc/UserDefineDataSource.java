package utils.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

/**
 * 自定义连接池编程步骤: 1.获取一个连接 2.将连接添加到集合中 3.封装一个方法,优先从集合中获取连接
 */
public class UserDefineDataSource {
    static String driver = "com.mysql.jdbc.Driver";
    static String url="jdbc:mysql://localhost:3306/anli";
    static String user="root";
    static String pwd = "235236";

    static Vector<Connection> pools = new Vector<Connection>();

    public static Connection getDBConnection(){
        try {
            //1.加载驱动
            Class.forName(driver);
            //2.取得数据库连接
            Connection conn =  DriverManager.getConnection(url, user, pwd);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 连接池
     */
    static {
        int i = 0;
        while(i<10){
            pools.add(getDBConnection());
            i++;
        }
    }

    public static synchronized Connection getPool(){
        if(pools != null && pools.size() > 0){
            int last_ind = pools.size() -1;
            Connection connection = pools.remove(last_ind);
            return connection;
        }else{
            return getDBConnection();
        }
    }

    public static int insert(String sql,Object[] params){
        Connection conn = getPool();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            for(int i=0;i<params.length;i++){
                pstmt.setObject(i+1, params[i]);
            }
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(conn != null){
//               conn.close();
                pools.add(conn);
            }
        }
        return 0;
    }

    public static void main(String[] args) {

    }
}