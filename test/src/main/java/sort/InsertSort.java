package sort;

import java.util.Arrays;

public class InsertSort {

    public static void main(String[] args) {

        int[] arr ={2,1,3,5,4};
        insertSort(arr);
        System.out.println(Arrays.toString(arr));

    }

    public static void insertSort(int[] a) {

        int len = a.length;//单独把数组长度拿出来，提高效率
        int insertNum;//要插入的数
        for (int i = 1; i < len; i++) { //因为arr[0]只有一个数,我们认为它是有序的，所以从1开始
            insertNum = a[i];//要插入的数
            int j = i - 1;//有序序列中最后一个元素的索引
            while (j >= 0 && a[j] > insertNum) {//从后往前循环，将有序序列中大于insertNum的数向后移动
                a[j + 1] = a[j];//元素向后移动
                j--;
            }
            a[j + 1] = insertNum;//找到位置，插入当前元素
        }
    }
}
