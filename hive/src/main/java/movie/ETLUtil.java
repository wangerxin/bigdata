package movie;

public class ETLUtil {
    public static String etlString(String ori) {

        //1.切割
        String[] split = ori.split("\t");
        //2.丢弃不合法数据
        int length = split.length;
        if (length < 9) {
            return null;
        }
        //3.处理分类字段中的空格
        split[3] = split[3].replace(" ", "");
        //4.处理相关视频id字段的&
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            //处理前九个字段
            if (i < 9) {
                if (i == split.length - 1) {
                    sb.append(split[i]);
                } else {
                    sb.append(split[i]).append("\t");
                }
            }
            //处理后面的字段
            else {
                if (i == split.length - 1) {
                    sb.append(split[i]);
                } else {
                    sb.append(split[i]).append("&");
                }
            }
        }
        return sb.toString();
    }
}
