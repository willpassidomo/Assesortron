package test.assesortron5;

import android.util.Log;

import junit.framework.TestCase;

/**
 * Created by otf on 7/29/15.
 */
public class PracticeTestTest extends TestCase {

    PracticeTest practiceTest;

    public void setUp() throws Exception {
        super.setUp();
        Log.i("Test", "setup");
        practiceTest = new PracticeTest();
    }

    public void tearDown() throws Exception {
        practiceTest = null;
    }

    public void testAddTogether() throws Exception {
        assertEquals(6, practiceTest.addTogether(2,4));
        assertEquals(4, practiceTest.addTogether(2,4));
    }
}