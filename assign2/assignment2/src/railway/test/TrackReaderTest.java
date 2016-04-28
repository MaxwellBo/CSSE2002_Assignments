package railway.test;

import railway.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic tests for the {@link TrackReader} implementation class.
 * 
 * CSSE7023: Write your own tests for the class here: I have only added a few
 * basic ones to get you started. You don't have to use these tests in your own
 * test suite.
 * 
 * A more extensive test suite will be performed for assessment of your code,
 * but this should get you started writing your own unit tests.
 */
public class TrackReaderTest {

    /**
     * Check reading a correctly formatted file with many sections.
     */
    @Test
    public void testCorrectlyFormattedManySections() throws Exception {
        Section[] sections =
                {
                        new Section(9, new JunctionBranch(new Junction("j0"),
                                Branch.FACING), new JunctionBranch(
                                new Junction("j1"), Branch.FACING)),

                        new Section(777, new JunctionBranch(new Junction("j2"),
                                Branch.NORMAL), new JunctionBranch(
                                new Junction("j4"), Branch.FACING)),

                        new Section(10, new JunctionBranch(new Junction("j5"),
                                Branch.NORMAL), new JunctionBranch(
                                new Junction("j6"), Branch.REVERSE)) };

        Track actualTrack = TrackReader.read("test_01_correctlyFormatted.txt");
        checkTrackSections(actualTrack, new HashSet<Section>(Arrays
                .asList(sections)));
    }

    /**
     * Check reading an incorrectly formatted file: a section is missing
     * information on a line
     */
    @Test
    public void testIncorrectlyFormattedMissingInformation() throws Exception {
        // Error on line 3: the line is blank. Each line should denote a
        // section and all section information is missing from that line.
        try {
            TrackReader.read("test_02_incorrectlyFormatted.txt");
            Assert.fail("FormatException not thrown");
        } catch (FormatException e) {
            // OK
        }
    }

    // -----Helper Methods-------------------------------

    /**
     * Checks that the given track has all, and only the expected sections.
     * 
     * @param track
     *            The track whose sections will be checked.
     * @param expectedSections
     *            The expected sections that the track should have
     */
    private void checkTrackSections(Track track, Set<Section> expectedSections) {
        Set<Section> actualSections = new HashSet<>();
        for (Section section : track) {
            actualSections.add(section);
        }
        Assert.assertEquals(expectedSections, actualSections);
    }

}
