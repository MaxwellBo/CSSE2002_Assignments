package railway.test;

import railway.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic tests for the {@link Section} implementation class.
 * 
 * Write your own junit4 tests for the class here.
 */
public class SectionTest {

    // TODO: Comment
    @Test
    public void testBasicSection() {
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.REVERSE);

        Section section1 = new Section(5, endPoint1, endPoint2);

        Assert.assertEquals(5, section1.getLength());
    }

//    @Test(expected = NullPointerException.class)
}
