package kfs.interval;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Class with two timestamps and operations with this interval. For situation of two interval can be
 * on next "draw" schema
 * <ul>
 * <li><b>C1</b> or <b>C2</b> two intervals don't have interaction</li>
 * <li><b>B1</b>, <b>B2</b>, <b>B3</b>, <b>B4</b> two intervals have some intersection</li>
 * </ul>
 *
 * <pre>
 * //  a:   ----------F--------------------------T----------   // base
 * // 
 * //  c1:  -F--T-----|--------------------------|----------   // not intersect
 * //  c2:  ----------|--------------------------|---F-T----   // not intersect
 * //
 * //  b1:  ---F------|---------T----------------|----------   // intersect
 * //  b2:  -------F--|--------------------------|--T-------   // intersect
 * //  b3:  ----------|-----------------F--------|--T-------   // intersect
 * //  b4:  ----------|-----F---------T----------|----------   // intersect
 * </pre>
 *
 * @author pavedrim
 */
public class kfsOneInterval {

    private final Date from;
    private final Date to;

    kfsOneInterval(Date from, Date to) {
        if (from.before(to)) {
            this.from = from;
            this.to = to;
        } else {
            this.from = to;
            this.to = from;
        }
    }

    public Long getSeconds() {
        return to.getTime() - from.getTime();
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    /**
     * Test if two intervals in not situation C1 or C2
     * <pre>
     * a:   ----------F--------------------------T----------   // base
     * c1:  -F--T-----|--------------------------|----------   // not intersect
     * c2:  ----------|--------------------------|---F-T----   // not intersect
     * </pre>
     *
     * @param other other interval
     * @return true if intervals has instersect
     */
    public boolean isIntersect(kfsOneInterval other) {
        // not c1 or c2
        if (from.equals(other.to) || (to.equals(other.from)))
            return true;
        return !(from.after(other.to) || to.before(other.from));
    }

    /**
     * Test if two interval is situation on schema
     * <pre>
     * a:   ----------F--------------------------T----------   // base
     * b1:  ---F------|---------T----------------|----------   // intersect
     * </pre>
     *
     * @param ot other interval
     * @return true if two intervals is situation B1
     */
    public boolean isIntersectB1(kfsOneInterval ot) {
        return (from.equals(ot.from)||from.after(ot.from)) && (to.equals(ot.to) || to.after(ot.to));
    }

    /**
     * Test if two interval is situation on schema
     * <pre>
     * a:   ----------F--------------------------T----------   // base
     * b2:  -------F--|--------------------------|--T-------   // intersect
     * </pre>
     *
     * @param ot other interval
     * @return true if two intervals is situation B2
     */
    public boolean isIntersectB2(kfsOneInterval ot) {
        return (from.equals(ot.from) || from.after(ot.from)) && (to.equals(ot.to) || to.before(ot.to));
    }

    /**
     * Test if two interval is situation on schema
     * <pre>
     * a:   ----------F--------------------------T----------   // base
     * b3:  ----------|-----------------F--------|--T-------   // intersect
     * </pre>
     *
     * @param ot other interval
     * @return true if two intervals is situation B3
     */
    public boolean isIntersectB3(kfsOneInterval ot) {
        return (from.equals(ot.from) || from.before(ot.from)) && (to.equals(ot.to) || to.before(ot.to));
    }

    /**
     * Test if two interval is situation on schema
     * <pre>
     * a:   ----------F--------------------------T----------   // base
     * b4:  ----------|-----F---------T----------|----------   // intersect
     * </pre>
     *
     * @param ot other interval
     * @return true if two intervals is situation B4
     */
    public boolean isIntersectB4(kfsOneInterval ot) {
        return (from.equals(ot.from) ||from.before(ot.from)) && (to.equals(ot.to) || to.after(ot.to));
    }

    public static List<kfsOneInterval> minus(kfsOneInterval a, kfsOneInterval ot) throws kfsIntervalException {
        if (a.isIntersect(ot)) {
            if (a.isIntersectB1(ot)) {
                if (ot.to.equals(a.to)) {
                    return Arrays.<kfsOneInterval>asList();
                }
                return Arrays.<kfsOneInterval>asList(new kfsOneInterval(ot.to, a.to));
            }
            if (a.isIntersectB2(ot)) {
                return Arrays.<kfsOneInterval>asList();
            }
            if (a.isIntersectB3(ot)) {
                return Arrays.<kfsOneInterval>asList(new kfsOneInterval(a.from, ot.from));
            }
            if (a.isIntersectB4(ot)) {
                return Arrays.<kfsOneInterval>asList(new kfsOneInterval(a.from, ot.from),
                        new kfsOneInterval(ot.to, a.to));
            }
        }
        throw new kfsIntervalException("Cannot create difference of two not intersect intervals");
    }

    public static kfsOneInterval intersect(kfsOneInterval a, kfsOneInterval ot) throws kfsIntervalException {
        if (a.isIntersect(ot)) {
            if (a.isIntersectB1(ot)) {
                return new kfsOneInterval(a.from, ot.to);
            }
            if (a.isIntersectB2(ot)) {
                return a;
            }
            if (a.isIntersectB3(ot)) {
                return new kfsOneInterval(ot.from, a.to);
            }
            if (a.isIntersectB4(ot)){
                return ot;
            }
        }
        throw new kfsIntervalException("Cannot create intersect");
    }

    public static kfsOneInterval union(kfsOneInterval a, kfsOneInterval b) throws kfsIntervalException {
        if (a.isIntersect(b)) {
            Date f = (a.from.before(b.from)) ? a.from : b.from;
            Date t = (a.to.after(b.to)) ? a.to : b.to;
            return new kfsOneInterval(f, t);
        } else {
            throw new kfsIntervalException("Cannot join two not intersect intervals");
        }
    }
}
