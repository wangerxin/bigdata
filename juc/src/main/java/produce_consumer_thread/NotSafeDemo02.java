package produce_consumer_thread;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 1 故障现象: java常用集合AaaryList,HashMap等是线程不安全的, 在并发访问时会报异常
 *              java.util.ConcurrentModificationException
 *
 * 2 导致原因 : 线程不安全
 *
 *  3 解决方案 :
 *  3.1 使用线程安全的集合代替 : new Vector<>();
 *                               CopyOnWriteArrayList();
 *  3.2 使用工具类将线程不安全的转为线程安全的: Collections.synchronizedList(new ArrayList<>());
 */
public class NotSafeDemo02
{
    public static void main(String[] ars)
    {
        // 资源类
        Map<String,String> map = new ConcurrentHashMap();

        // 多线程操作资源类
        for (int i = 1; i <=10; i++)
        {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(),UUID.randomUUID().toString().substring(0,8));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }

    // set集合案例
    public static void setNotSafe()
    {
        Set<String> set = new CopyOnWriteArraySet();

        for (int i = 1; i <=30; i++)
        {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }

    // list聚合案例
    public static void listNotSafe()
    {
        List<String> list = new CopyOnWriteArrayList();//Collections.synchronizedList(new ArrayList<>());//new Vector<>();//new ArrayList<>();

        for (int i = 1; i <=30; i++)
        {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}

/**笔记
 * 写时复制
 CopyOnWrite容器即写时复制的容器。往一个容器添加元素的时候，不直接往当前容器Object[]添加，而是先将当前容器Object[]进行Copy，
 复制出一个新的容器Object[] newElements，然后新的容器Object[] newElements里添加元素，添加完元素之后，
 再将原容器的引用指向新的容器 setArray(newElements);。这样做的好处是可以对CopyOnWrite容器进行并发的读，
 而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器
 public boolean add(E e)
 {
     final ReentrantLock lock = this.lock;
     lock.lock();

         try
 {
             Object[] elements = getArray();
             int len = elements.length;
             Object[] newElements = Arrays.copyOf(elements, len + 1);
             newElements[len] = e;
             setArray(newElements);
             return true;
         }
         finally {
            lock.unlock();
     }
 }
 */