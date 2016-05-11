package railway;

import java.io.*;

/**
 * Provides a method to read a track from a text file.
 */
public class TrackReader {

    /**
     * <p>
     * Reads a text file named fileName that describes the sections on a track,
     * and returns a track containing each of the sections in the file.
     * </p>
     * 
     * <p>
     * The file contains zero or more lines, each of which corresponds to a
     * section on the track.
     * 
     * Each line should contain five items separated by one or more whitespace
     * characters: a positive integer representing the length of the section,
     * followed by the name of a first junction, then the type of a first
     * branch, followed by the name of a second junction, and then the type of a
     * second branch. The section denoted by the line has the given length, and
     * two end-points: one constructed from the first junction and first branch
     * on the line, and the other constructed from the second junction and
     * section branch.
     * 
     * A junction name is simply an unformatted non-empty string that doesn't
     * contain any whitespace characters. The type of a branch is one of the
     * three strings "FACING", "NORMAL" or "REVERSE", which correspond to the
     * branches Branch.FACING, Branch.NORMAL, and Branch.REVERSE, respectively.
     * 
     * There may be leading or trailing whitespace on each line of the file.
     * (Refer to the Character.isWhitespace() method for the definition of a
     * white space in java.)
     * 
     * For example, the line <br>
     * <br>
     * 
     * 10 j1 FACING j2 NORMAL
     * 
     * <br>
     * <br>
     * denotes a section with length 10 and end-points (j1, FACING) and (j2,
     * NORMAL).
     * </p>
     * 
     * <p>
     * No two lines of the file should denote equivalent sections (as defined by
     * the equals method of the Section class), and no two sections described by
     * the input file should have a common end-point (since each junction can
     * only be connected to at most one section on each branch on a valid
     * track).
     * </p>
     * 
     * <p>
     * The method throws an IOException if there is an input error with the
     * input file (e.g. the file with name given by input parameter fileName
     * does not exist); otherwise it throws a FormatException if there is an
     * error with the input format (this includes the case where there is a
     * duplicate section, and the case where two or more sections have a common
     * end-point), otherwise it returns a track that contains each of the
     * sections described in the file (and no others).
     * 
     * If a FormatException is thrown, it will have a meaningful message that
     * accurately describes the problem with the input file format, including
     * the line of the file where the problem was detected.
     * </p>
     * 
     * @param fileName
     *            the file to read from
     * @return a track containing the sections from the file
     * @throws IOException
     *             if there is an error reading from the input file
     * @throws FormatException
     *             if there is an error with the input format. The
     *             FormatExceptions thrown should have a meaningful message that
     *             accurately describes the problem with the input file format,
     *             including the line of the file where the problem was
     *             detected.
     */
    public static Track read(String fileName) throws IOException,
            FormatException {
        Track collector = new Track();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            int lineCount = 0;

            while ((line = br.readLine()) != null) {
                lineCount++;

                // Remove "trailing and leading" whitespace
                line = line.trim();

                // Split on any whitespace,
                // where whitespace is regex "\\s"
                // where "\\s+" <-> "[ \\t\\n\\x0B\\f\\r]+"
                String[] splitLine = line.split("\\s+");


                try {
                    if (splitLine.length != 5) {
                        // Early panic
                        throw new FormatException(
                                "Invalid number of arguments specified on line");
                    }

                    JunctionBranch fst = new JunctionBranch(
                            new Junction(splitLine[1])
                            , parseBranch(splitLine[2]));

                    JunctionBranch snd = new JunctionBranch(
                            new Junction(splitLine[3])
                            , parseBranch(splitLine[4]));

                    Section toAdd = new Section(
                            Integer.parseInt(splitLine[0])
                            , fst
                            , snd);

                    if (collector.contains(toAdd)) {
                        // Early panic
                        throw new FormatException(
                                "Attempt to add duplicate section");
                    }

                    // Fallthrough to
                    collector.addSection(toAdd);
                }
                catch (Exception e) {
                    // Rethrow all Exceptions as FormatExceptions
                    throw new FormatException(
                            "The exception "
                                + "\"" + e.getMessage() + "\""
                                + " was caused by line "
                                + Integer.toString(lineCount)
                                + " of "
                                + fileName);
                }
            }
        }
        return collector;
    }

    /**
     * Parses the string argument as a member of the Branch Enum. The string to
     * be parsed must be equivalent to a the name of a member of the
     * Branch Enum.
     *
     * @param string
     *      the string to parse
     * @return a member of the Branch enum where the member name and string
     *         are equivalent
     * @throws FormatException if the string is no member of the Branch enum
     *                         with a name that is equivalent to the string
     */
    private static Branch parseBranch(String string) throws FormatException {
        switch (string) {
            case "FACING":
                return Branch.FACING;
            case "NORMAL":
                return Branch.NORMAL;
            case "REVERSE":
                return Branch.REVERSE;
            default:
                throw new FormatException(string
                        + " is not a valid Branch type");
        }
    }
}
