package kfs.interval;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    // 4)   G--A=========B--F           // compleetly in
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
        assertFalse("This interval Cannot touch (1)", ba.isIntersect(ts));
        assertFalse("This interval Cannot touch (1.1)", ts.isIntersect(ba));

        ts = new kfsOneInterval(E, F);
        assertTrue("This interval Must touch (2)", ba.isIntersect(ts));
        assertTrue("This interval Must touch (2.1)", ts.isIntersect(ba));

        ts = new kfsOneInterval(E, G);
        assertTrue("This interval Must touch (3)", ba.isIntersect(ts));
        assertTrue("This interval Must touch (3.1)", ts.isIntersect(ba));
        ts = new kfsOneInterval(G, E);
        assertTrue("This interval Must touch (3.2)", ba.isIntersect(ts));
        assertTrue("This interval Must touch (3.3)", ts.isIntersect(ba));

        ts = new kfsOneInterval(G, F);
        assertTrue("This interval Must touch (4)", ba.isIntersect(ts));
        assertTrue("This interval Must touch (4.1)", ts.isIntersect(ba));

        ts = new kfsOneInterval(K, L);
        assertTrue("This interval Must touch (5)", ba.isIntersect(ts));
        assertTrue("This interval Must touch (5.1)", ts.isIntersect(ba));
    }

    public void testUnion() throws kfsIntervalException {
        kfsOneInterval ba = new kfsOneInterval(A, B);
        kfsOneInterval ts = new kfsOneInterval(E, F);
        kfsOneInterval tq = kfsOneInterval.union(ba, ts);
        assertEquals("Union (2) From must be A", tq.getFrom(), A);
        assertEquals("Union (2) To must be F", tq.getTo(), F);
    }

    public void testIntersect() throws kfsIntervalException {
        // A----E====B--F
        kfsOneInterval ba = new kfsOneInterval(A, B);
        kfsOneInterval ef = new kfsOneInterval(E, F);
        kfsOneInterval in = kfsOneInterval.intersect(ba, ef);
        assertEquals("Intersect (2) From must be E", E, in.getFrom());
        assertEquals("Intersect (2) To must be B", B, in.getTo());
    }

    public void testMinus() throws kfsIntervalException {
        // A----E====B--F
        kfsOneInterval ba = new kfsOneInterval(A, B);
        kfsOneInterval ef = new kfsOneInterval(E, F);
        kfsOneInterval gf = new kfsOneInterval(G, F);
        kfsOneInterval kl = new kfsOneInterval(K, L);
        List<kfsOneInterval> inl = kfsOneInterval.minus(ba, ef);
        assertSame("Minus (2) output list size must be 1", 1, inl.size());
        kfsOneInterval in = inl.get(0);
        assertEquals("Intersect (2) From must be A", A, in.getFrom());
        assertEquals("Intersect (2) To must be E", E, in.getTo());    
    
        inl = kfsOneInterval.minus(gf, ba);
        assertSame("Minus (4) output list size must be 2", 2, inl.size());
        
        inl = kfsOneInterval.minus(ba, kl);
        assertSame("Minus (5) output list size must be 0", 0, inl.size());        
        
    }
    
}
