package kfs.interval;

/**
 *
 * @author pavedrim
 */
public class kfsIntervalException extends Exception {

    /**
     * Creates a new instance of
     * <code>kfsIntervalException</code> with detail message and source exception.
     *
     * @param msg the detail message.
     * @param ex source exception.
     */
    public kfsIntervalException(String msg, Throwable ex) {
        super(msg, ex);
    }

    /**
     * Constructs an instance of
     * <code>kfsIntervalException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public kfsIntervalException(String msg) {
        super(msg);
    }
}
