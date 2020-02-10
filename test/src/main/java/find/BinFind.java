package find;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BinFind {

    public static void main(String[] args) {

        int[] arr = {1, 1, 1, 1, 1, 1};
        System.out.println(binFind(arr, 0, arr.length - 1, 1));


    }


    /**
     * 二分查找, 查找指定数据所在的位置
     *
     * @param arr
     * @param start
     * @param end
     * @param key
     * @return
     */
    public static int binFind(int[] arr, int start, int end, int key) {

        //校验, 防止数据不在数组中
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
