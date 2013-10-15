package kfs.interval;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author pavedrim
 */
public class kfsOneIntervalList implements Iterable<kfsOneInterval> {

    private final ArrayList<kfsOneInterval> lst;

    public kfsOneIntervalList() {
        lst = new ArrayList<kfsOneInterval>();
    }

    private kfsOneIntervalList(ArrayList<kfsOneInterval> lst) {
        this.lst = lst;
    }

    public void add(Date from, Date to) {
        lst.add(new kfsOneInterval(from, to));
    }

    public Iterator<kfsOneInterval> iterator() {
        return lst.iterator();
    }

    public kfsOneIntervalList normalize() throws kfsIntervalException {
        return new kfsOneIntervalList(normalize(new ArrayList<kfsOneInterval>(lst)));
    }

    private static boolean normalizeInt(ArrayList<kfsOneInterval> lst) throws kfsIntervalException {
        if (lst.size() > 1) {
            for (kfsOneInterval o1 : lst) {
                for (kfsOneInterval o2 : lst) {
                    if (o1 != o2) {
                        if (o1.isIntersect(o2)) {
                            lst.remove(o1);
                            lst.remove(o2);
                            lst.add(kfsOneInterval.union(o1, o2));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static ArrayList<kfsOneInterval> normalize(ArrayList<kfsOneInterval> lst) throws kfsIntervalException {
        if (lst.size() <= 1) {
            return lst;
        }
        while (normalizeInt(lst)) {
        }
        return lst;
    }

    public kfsOneIntervalList minus(kfsOneInterval mVal) throws kfsIntervalException {
        ArrayDeque<kfsOneInterval> in = new ArrayDeque<kfsOneInterval>(lst);
        ArrayDeque<kfsOneInterval> out;
        boolean repl1 = true;
        while (repl1) {
            repl1 = false;
            out = new ArrayDeque<kfsOneInterval>();
            while (!in.isEmpty()) {
                kfsOneInterval ii = in.pop();
                if (ii.isIntersect(mVal)) {
                    out.addAll(kfsOneInterval.minus(ii, mVal));
                    repl1 = true;
                } else {
                    out.push(ii);
                }
            }
            in = out;
        }
        lst.clear();
        lst.addAll(new ArrayList<kfsOneInterval>(in));
        return this;
    }
}
