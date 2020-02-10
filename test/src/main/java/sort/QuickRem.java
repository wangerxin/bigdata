package sort;

public class QuickRem {

    public static void main(String[] args) {

        int[] arr = {1, 2, 2, -1, 5, -2, 3, 3, 3};

        System.out.println("排序之前：\n" + java.util.Arrays.toString(arr));

        quickSort(arr, 0, arr.length - 1);

        System.out.println("排序之后：\n" + java.util.Arrays.toString(arr));
    }

    /**
     * 快速排序
     *
     * @param arr
     * @param left
     * @param right
     */
    private static void quickSort(int[] arr, int left, int right) {

        //1.判断
        if (left > right) return;

        //2.定义基准数
        int base = arr[left];
        //2.定义指针
        int i = left;
        int j = right;

        //3. 指针j左移, 指针i右移, 直到指针i,j重合时停止循环
        while (i < j) {

            //j指针左移,直到arr[j] < base 停下
            while (j > i && arr[j] >= base) {
                j--;
            }

            //i指针右移, 直到arr[i] > base 停下
            while (i < j && arr[i] <= base) {
                i++;
            }

            // 当ij都停下时,交换ij位置的数据
            if (i < j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        //当指针i,j重合时, 交换基准数和 arr[i]
        arr[left] = arr[i];
        arr[i] = base;

        //递归
        quickSort(arr, left, i - 1);
        quickSort(arr, i + 1, right);
    }
}