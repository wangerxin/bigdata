
import utils.linux.ExecShellOrder;
import utils.jdbc.DataSourceUtil;

import java.util.List;
import java.util.Map;

/**
 * 功能: mysql导入hive,自动处理新增字段
 * 1.获取mysql最新表字段,如果添加了新字段,在hive中添加新字段
 * 2.拼接sqoop语句
 * 3.执行sqoop语句
 * 问题: sqoop语句执行时顺时进行,效率较低.
 *
 * 如果不用自动处理,就只能手动处理了
 * 1.在hive中新增字段
 * 2.修改sqoop脚本,新增字段
 *
 * sqoop常见问题: https://blog.csdn.net/weixin_42496757/article/details/88096925
 * 快速同步时间: https://blog.csdn.net/whdxjbw/article/details/80805734
 *
 */
public class SqoopImport {

    public String sqoop_sh = "";

    public static void main(String[] args) {
        Map<String, Map<String, String>> tableNameAndColumn = DataSourceUtil.getTableNameAndColumn("jdbc:mysql://localhost:3306/anli", "root", "235236");

        // 获取表的所有列
        String order = String.format("sh hive -e \"use gmall;desc %s;\" 2>/dev/null | egrep -v \"#|dt|^\\s+$\" | awk \"{print $1}\"\n","tableName" );
        Map<Integer, List<String>> execResult = ExecShellOrder.getExecResult(order);
    }

    // sh hive -e "use gmall;desc ods_base_category1;" 2>/dev/null | egrep -v "#|dt|^\s+$" | awk '{print $1}'
    //hive -e "USE ${dname}; DESC ${tname};" 2>/dev/null | egrep -v "^#|^uid |^ds |^${utime_col} |^\s+$" | awk '{print $1}'
    public static void execLinux(String exec) {

    }

}
