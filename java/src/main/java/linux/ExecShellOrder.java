package linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecShellOrder {

    public static void main(String[] args) {
        Map<Integer, List<String>> execResultMap = getExecResult(args[0]);
        for (Map.Entry<Integer, List<String>> entry : execResultMap.entrySet()) {
            Integer key = entry.getKey();
            String value = "";
            for (String s : entry.getValue()) {
                value = s + ",";
            }
            System.out.println(key + ":" + value);
        }
    }
    public static Map<Integer, List<String>> getExecResult(String order) {
        Map<Integer, List<String>> resultMap = new HashMap<>();
        try {
            //执行命令
            Process process = Runtime.getRuntime().exec(order);

            //获取执行结果
            int status = process.waitFor();
            List<String> resultList = new ArrayList<String>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                resultList.add(line);
            }
            bufferedReader.close();

            //封装返回结果
            resultMap.put(status, resultList);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
