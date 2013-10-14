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
}
