package railway;

import java.util.*;

/**
 * <p>
 * An immutable class corresponding to a section of a railway track.
 * </p>
 * 
 * <p>
 * A section of track has a positive length (in meters), and lies between two
 * junctions. These junctions may not be distinct, since a section of track may
 * form a loop from one junction back to the same junction.
 * </p>
 * 
 * <p>
 * In the context of a particular railway track, each junction has between one
 * and three branches (of type Branch) that connect it to sections of the track.
 * It can have at most one branch of each type. (I.e. a junction may not have
 * two branches of type Branch.FACING.)
 * </p>
 * 
 * <p>
 * If a section forms a loop from one junction back to itself, then the junction
 * must be connected to the section on two different branches.
 * </p>
 * 
 * <p>
 * A section is uniquely identified by its length and two distinct end-points,
 * where an end-point is a junction and the branch that connects it to the
 * section.
 * </p>
 */
public class Section {

    /**
     * The length of the section.
     */
    private final int length;

    /**
     * A collection of the two distinct end-points of the section,
     * which are unique branches of a junction.
     */
    private final HashSet<JunctionBranch> endPoints;

    /*
     * invariant: endPoints != null
     *
     *        && !(endPoints.contains(null))
     *
     *        && length > 0
     *
     *        && endPoint1 != endPoint2
     *
     *        && Both endPoints maintain their post-conditions and invariants
     */

    /**
     * Creates a new section with the given length (in meters) and end-points.
     * 
     * @param length
     *            a positive integer representing the length of the section in
     *            meters
     * @param endPoint1
     *            one end-point of the section
     * @param endPoint2
     *            the other end-point of the section
     * @throws NullPointerException
     *             if either end-point is null
     * @throws IllegalArgumentException
     *             if either the length is less than or equal to zero, or the
     *             end-points are equivalent (two end-points are equivalent if
     *             they are equal according to the equals method of the
     *             JunctionBranch class).
     */
    public Section(int length, JunctionBranch endPoint1,
            JunctionBranch endPoint2) throws NullPointerException,
            IllegalArgumentException {

        if (endPoint1 == null) {
            throw new NullPointerException("endPoint1 parameter is null");
        }
        else if (endPoint2 == null) {
            throw new NullPointerException("endPoint2 parameter is null");
        }
        else if (length <= 0) {
            throw new IllegalArgumentException("length parameter is less than"
                    + " or equal to zero");
        }
        else if (endPoint1.equals(endPoint2)) {
            throw new IllegalArgumentException("endPoint1 parameter"
                    + " is equal to endPoint2 parameter");
        }
        else {
            this.length = length;
            this.endPoints = new HashSet<>();
            endPoints.add(endPoint1);
            endPoints.add(endPoint2);
        }
    }

    /**
     * Returns the length of the section (in meters).
     * 
     * @return the length of the section
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the end-points of the section.
     * 
     * @return a set of the end-points of the section.
     */
    public Set<JunctionBranch> getEndPoints() {
        return new HashSet<>(endPoints);
    }

    /**
     * If the given end-point is equivalent to an end-point of the section, then
     * it returns the end-point at the opposite end of the section. Otherwise
     * this method throws an IllegalArgumentException.
     * 
     * @param endPoint
     *            the given end-point of this section
     * @throws IllegalArgumentException
     *             if the given end-point is not an equivalent to an end-point
     *             of the given section.
     * @return the end-point at the opposite end of the section to endPoint
     */
    public JunctionBranch otherEndPoint(JunctionBranch endPoint) {
        if (endPoints.contains(endPoint)) {
            for (JunctionBranch i : endPoints) {
                // search for opposite
                if (!i.equals(endPoint)) {
                    return i; // early return the other end-point
                }
            }
        }
        // fallthrough to error
        throw new IllegalArgumentException("endPoint parameter"
                + " is not an equivalent to an end-point"
                + " of the given section");
    }

    /**
     * <p>
     * Returns the string representation of the section. The string
     * representation consists of the length, followed by the single space
     * character ' ', followed by the toString() representation of one of the
     * end-points, followed by the single space character ' ', followed by the
     * toString() representation of the other end-point.
     * </p>
     *
     * <p>
     * The end-points can occur in any order, so that either the string
     * "9 (j1, FACING) (j2, NORMAL)" or the string
     * "9 (j2, NORMAL) (j1, FACING)", would be valid string representations of a
     * section of length 9, with end-points "(j1, FACING)" and "(j2, NORMAL)".
     * </p>
     */
    @Override
    public String toString() {
        String base = Integer.toString(length);
        for (JunctionBranch i : endPoints) {
            base += " " + i.toString();
        }
        return base;
    }

    /**
     * <p>
     * Returns true if and only if the given object is an instance of the class
     * Section with the same length as this one, and equivalent end-points.
     * </p>
     *
     * <p>
     * The end-points of Section a and Section b are equivalent if and only if,
     * for each end-point of a, there is an equivalent end-point of b. (Two
     * end-points are equivalent if their junctions and branches are equivalent
     * as per the equals method in the JunctionBranch class).
     * </p>
     */
    @Override
    public boolean equals(Object o) {
        // Check reference equality
        if (this == o) {
            return true;
        }

        // Verify that o can be coerced without throwing errors
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // Coerce the Object into a Section
        Section section = (Section) o;

        if (length != section.length) {
            return false; // early return if length not equal
        }

        return endPoints.equals(section.endPoints);
    }

    @Override
    public int hashCode() {
        // start with length as the collector
        int result = length;
        // introduce the hashcode of the end-points collection
        result = 31 * result + endPoints.hashCode();
        return result;
    }

    /**
     * Determines whether this class is internally consistent (i.e. it satisfies
     * its class invariant).
     * 
     * This method is only intended for testing purposes.
     * 
     * @return true if this class is internally consistent, and false otherwise.
     */
    public boolean checkInvariant() {
        // short circuit on invariant violation
        if (endPoints == null
                || endPoints.contains(null)
                || endPoints.size() != 2
                || length <= 0
                || endPoints.stream().anyMatch(i -> !i.checkInvariant())) {
            // if any invariants return not true (false), return false
            return false;
        }
        // proceed to further checks

        // "endPoint1 != endPoint2"
        Object[] endPointsArray = endPoints.toArray();
        return !endPointsArray[0].equals(endPointsArray[1]);
    }
}
