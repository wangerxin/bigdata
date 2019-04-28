package topN;

import java.util.*;

public class MapTest {
    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(1, "aa");
        treeMap.put(3, "cc");
        treeMap.put(5, "ee");
        treeMap.put(4, "dd");

        treeMap.remove(treeMap.firstKey());
        Iterator<Integer> keyIterator = treeMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            Integer key = keyIterator.next();
            System.out.println(keyIterator.next() + ":" + treeMap.get(keyIterator.next()));
        }
    }
}
