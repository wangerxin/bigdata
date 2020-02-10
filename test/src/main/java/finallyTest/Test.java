package finallyTest;

 class Test{
    
        long  a[] = new long[10];

    public static void main(String[ ] args){
        System.out.println(init());

    }
     static int init(){
        try{
            return 1/0;
        }catch (Exception e){
            System.out.println("error");
            return -1;
        }finally{
            return 1;
        }
    }
}