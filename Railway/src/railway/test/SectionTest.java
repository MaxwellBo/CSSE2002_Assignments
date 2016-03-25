package railway.test;

import org.junit.Before;
import railway.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

/**
 * Basic tests for the {@link Section} implementation class.
 * 
 * Write your own junit4 tests for the class here.
 */
public class SectionTest {

    JunctionBranch endPoint1F;
    JunctionBranch endPoint1N; // To test loops
    JunctionBranch endPoint2R;
    JunctionBranch endPoint1Fc; // Same params as endPoint1F
    JunctionBranch endPoint2Rc; // To test for non-reference equality
    JunctionBranch endPoint3N;

    @Before
    public void setUp() {
        endPoint1F =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        endPoint1N =
                new JunctionBranch(new Junction("j1"), Branch.NORMAL);
        endPoint2R =
                new JunctionBranch(new Junction("j2"), Branch.REVERSE);
        endPoint1Fc =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        endPoint2Rc =
                new JunctionBranch(new Junction("j2"), Branch.REVERSE);
        endPoint3N =
                new JunctionBranch(new Junction("j3"), Branch.NORMAL);

    }

    /** Test Section with valid parameters */
    @Test
    public void testBasicSection() {

        // setup
        Section section1 = new Section(5, endPoint1F, endPoint2R);

        // check length getter
        Assert.assertEquals(5, section1.getLength());

        // check endpoint set getter
        HashSet<JunctionBranch> endPoints12 = new HashSet<>();
        endPoints12.add(endPoint1F);
        endPoints12.add(endPoint2R);
        Assert.assertEquals(endPoints12, section1.getEndPoints());

        // check endpoint partner getter
        Assert.assertEquals(section1.otherEndPoint(endPoint1F), endPoint2R);

        // check toString method
        String s1s = section1.toString();
        String optionA = "5 (j1, FACING) (j2, REVERSE)";
        String optionB = "5 (j2, REVERSE) (j1, FACING)";
        Assert.assertTrue(s1s.equals(optionA) || s1s.equals(optionB));

        // check equals method
        Section section2 = new Section(5, endPoint1Fc, endPoint2Rc);
        Assert.assertTrue(section1.equals(section2));

        // check invariants
        Assert.assertTrue(section1.checkInvariant());
    }

    @Test
    public void testConstructorLoop() { // Both on same junction
        Section section1 = new Section(5, endPoint1F, endPoint1N);
        Assert.assertTrue(section1.checkInvariant());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNull () {
        Section section1 = new Section(5, endPoint1F, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeLength() {
        Section section1 = new Section(-5, endPoint1F, endPoint2R);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEquivalentEndpoints() {
        Section section1 = new Section(1, endPoint1F, endPoint1Fc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetterInvalidEndpointParam() {
        Section section1 = new Section(5, endPoint1F, endPoint2R);
        section1.otherEndPoint(endPoint3N);
    }
}
