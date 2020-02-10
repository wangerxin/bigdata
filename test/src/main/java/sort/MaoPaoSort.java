package sort;

import java.util.Arrays;

/**
 * 冒泡排序
 */
public class MaoPaoSort {

    public static void main(String[] args) {

        int[] array = new int[]{2, 4, 1, 6, 2};

        //第一个for循环,表示有i轮比较
        for (int i = 0; i < array.length - 1; i++) {

            //第二个for循环, 表示每一轮比较j次
            for (int j = 0; j < array.length - 1 - i; j++) {
                int temp = 0;
                if (array[j]> array[j+1]){

                    temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
            }
        }

        System.out.println(Arrays.toString(array));
    }
}
