
import multhreadfiletransport.server.core.ServerCenter;
import multhreadfiletransport.server.distribution.ResourceTable;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.net.Socket;
import java.util.*;

/**
 * Created by dela on 1/25/18.
 */
public class test {
    public static void main(String[] args) {
        List<Map<String, List<Integer>>> list = new ArrayList<>();

        for (int i = 0; i < 3 ; i++) {
            Map<String, List<Integer>> map = new HashMap<>();
            map.put(String.valueOf(i), new ArrayList<Integer>());
            list.add(map);
        }

        for (int i = 0; i < 3; i++) {
            List<Integer> temp = list.get(i).get(String.valueOf(i));

            temp.add(1);
            temp.add(2);
            temp.add(3);
        }

        System.out.println(list);

        //        ResourceTable resourceTable = new ResourceTable();
//
//        Jedis jedis = resourceTable.getJedis();
//
//        jedis.del("1", "2", "3");
//
//        String[] fileNames = {"1.c", "2.c", "3.c"};
//        jedis.sadd("1", fileNames);
////        System.out.println(jedis.smembers("1"));
//
//        String[] fileNames2 = {"4.c", "5.c", "6.c"};
//        jedis.sadd("2", fileNames2);
//
//        String[] fileNames3 = {"7.c", "8.c", "9.c"};
//        jedis.sadd("3", fileNames3);
//
//        String[] file = {"1", "2", "3"};
//        Set<String> result = jedis.sunion(file);
//
//        System.out.println(result);

//        Set<String> jset = jedis.smembers("5");
//
//        String[] str = {"a", "b"};
//
//        Set<String> set1 = new HashSet<>();
//        set1.add("1");
//        set1.add("2");
//
//        ServerCenter.clientIDMap.put("4", new Socket());
//        ServerCenter.clientIDMap.put("3", new Socket());
//
//        Set<String> set2 = ServerCenter.clientIDMap.keySet();
//        set1.retainAll(set2);
//        System.out.println(set1);

//        Collections.addAll(set1, str);
//
//        Set<String> set2 = new HashSet<>();
//        set2.add("1");
//        set2.add("2");
//        set2.add("5");

//        set1.removeAll(set2);

//        Set<String> set3 = new HashSet<>();
//        set3.addAll(set1);
//        set3.removeAll(set2);
//
//        System.out.println(set1);
//        System.out.println(set3);
//        System.out.println(jset.equals(set4));

//        List<String> fileNames = new ArrayList<>();
//        getFileNames("/home/dela/JavaData/zhuyidi", fileNames);
//        System.out.println(fileNames);
    }

    public static void getFileNames(String path, List<String> fileNames) {
        File file = new File(path);
        File [] files = file.listFiles();

        for (File a : files) {
            if (a.isDirectory()) {
                getFileNames(a.getAbsolutePath(), fileNames);
            } else {
                fileNames.add(a.getName());
            }
        }
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
