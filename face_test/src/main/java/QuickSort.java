public class QuickSort {

    public static void quickSort(int[] arr, int low, int high) {
        int left, right, base, temp;
        if (low > high) {
            return;
        }
        left = low;
        right = high;
        base = arr[low];

        while (left < right) {
            //先看右边，依次往左递减
            while (base <= arr[right] && left < right) {
                right--;
            }
            //再看左边，依次往右递增
            while (base >= arr[left] && left < right) {
                left++;
            }
            //如果满足条件则交换
            if (left < right) {
                temp = arr[right];
                arr[right] = arr[left];
                arr[left] = temp;
            }
        }
        //最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[left];
        arr[left] = base;
        //递归调用左半数组
        quickSort(arr, low, right - 1);
        //递归调用右半数组
        quickSort(arr, right + 1, high);
    }


    public static void main(String[] args) {
        int[] arr = {10, 7, 2, 4, 7, 62, 3, 4, 2, 1, 8, 9, 19};
        //quickSort(arr, 0, arr.length - 1);
        sort(arr,0,arr.length-1);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    public static void sort(int arr[],int left,int right){
        //校验
        if (left > right){
            return;
        }

        //赋值
        int i = left;
        int j = right;
        int base = arr[left];
        int temp;

        //交换基准位两边
        while (i < j){
            while (i < j && arr[j] >= base ){
                j--;
            }

            while (i < j && arr[i] <= base){
                i++;
            }

            if (i < j){
                temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
            }
        }

        //基准位归位
        arr[left] = arr[i];
        arr[i] = base;

        //递归
        sort(arr,left,i-1);
        sort(arr,i+1,right);

    }
}