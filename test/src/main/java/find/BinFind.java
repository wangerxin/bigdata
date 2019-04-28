package find;

public class BinFind {

    public static void main(String[] args) {

        int[] arr = {1,2,4,5,7,9};
        System.out.println(binFind(arr, 0, arr.length-1, 9));
    }

    // 二分查找
    public static int binFind(int[] arr, int start, int end, int key) {

        //简单校验
        if (key < arr[start] || key > arr[end]) {
            return -1;
        }

        //中间位
        int mid = (start + end) / 2;

        //将key与中间位比较
        if (key < arr[mid]) {
            return binFind(arr, start, arr[mid - 1], key);
        } else if (key > arr[mid]) {
            return binFind(arr, arr[mid + 1], end, key);
        } else {
            return mid;
        }
    }
}
