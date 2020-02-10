public class StrIsNumber {

    public static void main(String[] args) {

        System.out.println(isNumber("123a"));
    }

    public static boolean isNumber(String str) {

        try {
            double v = Double.parseDouble(str);
            return true;
        }catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
    }
}
