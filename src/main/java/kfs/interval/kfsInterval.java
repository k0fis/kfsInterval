package kfs.interval;

import java.util.Date;

/**
 *
 * @author pavedrim
 */
public class kfsInterval {

    private Long span;
    private final kfsOneIntervalList plus;
    private final kfsOneIntervalList minus;

    public kfsInterval() {
        plus = new kfsOneIntervalList();
        minus = new kfsOneIntervalList();
        span = -1l;
    }

    public kfsInterval plus(Date from, Date to) {
        plus.add(from, to);
        span = -1l;
        return this;
    }

    public kfsInterval minus(Date from, Date to) {
        minus.add(from, to);
        span = -1l;
        return this;
    }

    public Long getSpan() {
        if (span >= 0l) {
            return span;
        }
        span = 0l;
        plus.normalize();
        for (kfsOneInterval ii : minus.normalize()) {
            plus.minus(ii);
        }
        for (kfsOneInterval ii : plus) {
            span += ii.getSeconds();
        }
        return span;
    }
       

}
