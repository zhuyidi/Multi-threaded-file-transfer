import multhreadfiletransport.util.PackageUtil;
import multhreadfiletransport.util.ParseUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by dela on 1/25/18.
 */
public class test {
    public static void main(String[] args) {
        System.out.println(0 | 8 | (0 + 8) | (8 - (0 + 8)));
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
