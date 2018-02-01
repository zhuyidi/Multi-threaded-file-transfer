import multhreadfiletransport.util.PackageUtil;
import multhreadfiletransport.util.ParseUtil;

import java.util.*;

/**
 * Created by dela on 1/25/18.
 */
public class test {
    public static void main(String[] args) {
        System.out.println(0 | 8 | (0 + 8) | (8 - (0 + 8)));
        List<Integer> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();
        List<Integer> c = new ArrayList<>();
        List<Integer> d = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
    }
}

class aaa implements Comparable<aaa>{
    int i;

    public aaa(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public int compareTo(aaa o) {
        return this.i > o.i ? 1 : -1;
    }

    @Override
    public String toString() {
        return "aaa{" +
                "i=" + i +
                '}';
    }
}
