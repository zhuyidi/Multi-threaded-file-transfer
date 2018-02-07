
import multhreadfiletransport.server.distribution.ResourceTable;

import java.util.*;

/**
 * Created by dela on 1/25/18.
 */
public class test {
    public static void main(String[] args) {
        ResourceTable resourceTable = new ResourceTable();
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
