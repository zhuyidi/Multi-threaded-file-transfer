
import multhreadfiletransport.server.distribution.ResourceTable;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by dela on 1/25/18.
 */
public class test {
    public static void main(String[] args) {
        ResourceTable resourceTable = new ResourceTable();

//        Jedis jedis = resourceTable.getJedis();
//
//        jedis.del("1", "2");
//
//        String[] fileNames = {"1.c", "2.c", "3.c"};
//        jedis.sadd("1", fileNames);
//        System.out.println(jedis.smembers("1"));
//
//        Set<String> jset = jedis.smembers("5");
//
        String[] str = {"a", "b"};

        Set<String> set1 = new HashSet<>();
        set1.add("1");
        set1.add("2");
        Collections.addAll(set1, str);

        Set<String> set2 = new HashSet<>();
        set2.add("1");
        set2.add("2");
        set2.add("5");

//        set1.removeAll(set2);

        Set<String> set3 = new HashSet<>();
        set3.addAll(set1);
        set3.removeAll(set2);

        System.out.println(set1);
        System.out.println(set3);
//        System.out.println(jset.equals(set4));
    }
}

//class aaa implements Comparable<aaa>{
//    int i;
//
//    public aaa(int i) {
//        this.i = i;
//    }
//
//    public int getI() {
//        return i;
//    }
//
//    public void setI(int i) {
//        this.i = i;
//    }
//
//    @Override
//    public int compareTo(aaa o) {
//        return this.i > o.i ? 1 : -1;
//    }
//
//    @Override
//    public String toString() {
//        return "aaa{" +
//                "i=" + i +
//                '}';
//    }
//}
