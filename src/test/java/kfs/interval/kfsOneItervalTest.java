package kfs.interval;

import java.util.Calendar;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class kfsOneItervalTest extends TestCase {

    // 1)      A---------B      C---D   // completly out
    // 2)      A----E====B--F           // intersect
    // 3)   G--A====E----B              // intersect
    // 4)   G--A=========B--F           //  compleetly in
    // 5)      AK========BL             // compleetly same
    final Date A, B, C, D, E, F, G, H, J, K, L;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public kfsOneItervalTest(String testName) {
        super(testName);
        Calendar c = Calendar.getInstance();
        G = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 5);
        A = c.getTime();
        K = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        H = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        J = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        E = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        B = c.getTime();
        L = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        F = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        C = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 1);
        D = c.getTime();
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(kfsOneItervalTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testTouch() {
        kfsOneInterval ba = new kfsOneInterval(A, B);
        kfsOneInterval ts = new kfsOneInterval(C, D);
        assertFalse("This interval Cannot touch (1)", ba.isTouching(ts));
        assertFalse("This interval Cannot touch (1.1)", ts.isTouching(ba));

        ts = new kfsOneInterval(E, F);
        assertTrue("This interval Must touch (2)", ba.isTouching(ts));
        assertTrue("This interval Must touch (2.1)", ts.isTouching(ba));

        ts = new kfsOneInterval(E, G);
        assertTrue("This interval Must touch (3)", ba.isTouching(ts));
        assertTrue("This interval Must touch (3.1)", ts.isTouching(ba));
        ts = new kfsOneInterval(G, E);
        assertTrue("This interval Must touch (3.2)", ba.isTouching(ts));
        assertTrue("This interval Must touch (3.3)", ts.isTouching(ba));

        ts = new kfsOneInterval(G, F);
        assertTrue("This interval Must touch (4)", ba.isTouching(ts));
        assertTrue("This interval Must touch (4.1)", ts.isTouching(ba));

        ts = new kfsOneInterval(K, L);
        assertTrue("This interval Must touch (5)", ba.isTouching(ts));
        assertTrue("This interval Must touch (5.1)", ts.isTouching(ba));
    }

    public void createOneTest() throws kfsIntervalException {
        kfsOneInterval ba = new kfsOneInterval(A, B);
        kfsOneInterval ts = new kfsOneInterval(E, F);
        kfsOneInterval tq = kfsOneInterval.createOne(ba, ts);
        assertEquals("(2) From must be A", tq.getFrom(), A);
        assertEquals("(2) To must be F", tq.getTo(), F);
    }
}
