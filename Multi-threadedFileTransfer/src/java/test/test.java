import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by dela on 1/25/18.
 */
public class test {
    public static void main(String[] args) {
        LinkedList<aaa> list = new LinkedList();

        aaa a1 = new aaa(-1);
        aaa a2 = new aaa(1);
        aaa a3 = new aaa(0);


        list.add(a1);
        list.add(a2);

        int index = 0;
        for (aaa a : list) {
            if(a.getI() > a3.getI()) {
                index = list.indexOf(a);
            }
        }
        list.add(index, a3);
        System.out.println(list);
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
