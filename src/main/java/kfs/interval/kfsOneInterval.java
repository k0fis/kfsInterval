package kfs.interval;

import java.util.Date;

/**
 *
 * @author pavedrim
 */
public class kfsOneInterval {

    private final Date from;
    private final Date to;
    private final kfsISign sign;

    kfsOneInterval(Date from, Date to, kfsISign sign) {
        if (from.before(to)) {
            this.from = from;
            this.to = to;
        } else {
            this.from = to;
            this.to = from;
        }
        this.sign = sign;
    }

    public boolean isTouching(kfsOneInterval other) {
        return !(this.from.after(other.to) || this.to.before(other.from));
    }
    
    public Long getSeconds() {
        return to.getTime() - from.getTime();
    }
    
    public boolean isPlus() {
        return sign == kfsISign.plus;
    }

    public boolean isMinus() {
        return sign == kfsISign.minus;
    }
    
    public Date getFrom() {
        return from;
    }
    
    public Date getTo() {
        return to;
    }
}
