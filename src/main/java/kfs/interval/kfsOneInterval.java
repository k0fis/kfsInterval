package kfs.interval;

import java.util.Date;

/**
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

    public boolean isTouching(kfsOneInterval other) {
        return !(this.from.after(other.to) || this.to.before(other.from));
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

    public static kfsOneInterval createOne(kfsOneInterval a, kfsOneInterval b) throws kfsIntervalException {
        if (a.isTouching(b)) {
            Date f = (a.from.before(b.from)) ? a.from : b.from;
            Date t = (a.to.after(b.to)) ? a.to : b.to;
            return new kfsOneInterval(f, t);
        } else {
            throw new kfsIntervalException("Cannot join two not intersect intervals");
        }
    }
}
