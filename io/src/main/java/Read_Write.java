import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class Read_Write {

    public static void main(String[] args) throws Exception{


        //输入流
        FileInputStream fis = new FileInputStream("/Users/erxinwang/IdeaProjects/bigdata/io/src/main/file/sql");
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        //输出流
        FileOutputStream fos = new FileOutputStream(new File("/Users/erxinwang/IdeaProjects/bigdata/io/src/main/file/newsql"));
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);

        String line = "";
        while ((line = br.readLine()) != null) {

            if (line.startsWith("$")){
                String newLine = line.substring(1,line.length());
                bw.write(line.replace(",","") + " as " + newLine + "\n");
            }else {
                bw.write(line + "\n");
            }
        }

        br.close();
        isr.close();
        fis.close();

        bw.close();
        osw.close();
        fos.close();
        System.err.println("The server is ok,You can go to writePath!");
    }
}
