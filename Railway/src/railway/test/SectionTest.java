package railway.test;

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

    /** Test Section with valid parameters */
    @Test
    public void testBasicSection() {

        // setup
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.REVERSE);
        Section section1 = new Section(5, endPoint1, endPoint2);

        // check length getter
        Assert.assertEquals(5, section1.getLength());

        // check endpoint set getter
        HashSet<JunctionBranch> endPoints12 = new HashSet<>();
        endPoints12.add(endPoint1);
        endPoints12.add(endPoint2);
        Assert.assertEquals(endPoints12, section1.getEndPoints());

        // check endpoint partner getter
        Assert.assertEquals(section1.otherEndPoint(endPoint1), endPoint2);

        // check toString method
        String s1s = section1.toString();
        String optionA = "5 (j1, FACING) (j2, REVERSE)";
        String optionB = "5 (j2, REVERSE) (j1, FACIING)";
        Assert.assertTrue(s1s.equals(optionA) || s1s.equals(optionB));

        // check equals method
        JunctionBranch endPoint1a =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2a =
                new JunctionBranch(new Junction("j2"), Branch.REVERSE);
        Section section2 = new Section(5, endPoint1a, endPoint2a);

        Assert.assertTrue(section1.equals(section2));

        // check invariants
        Assert.assertTrue(section1.checkInvariant());
    }

//    @Test(expected = NullPointerException.class)
}
