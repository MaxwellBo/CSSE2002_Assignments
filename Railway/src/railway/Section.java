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

    private final int length;
    private final HashSet<JunctionBranch> endPoints;

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

        if (endPoint1 == null || endPoint2 == null) {
            throw new NullPointerException(); // TODO: Error message?
        }
        else if (length <= 0) {
            throw new IllegalArgumentException();
        }
        else if (endPoint1.equals(endPoint2)) {
            throw new IllegalArgumentException();
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
        return endPoints;
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
        // TODO: Can we assume reference equality here?
        // TODO: Or do I have to go defensive?
        if (!endPoints.contains(endPoint)) {
            throw new IllegalArgumentException();
        }
        else {
            HashSet<JunctionBranch> clonedEndPoints = new HashSet<>(endPoints);
            clonedEndPoints.remove(endPoint);
            return (JunctionBranch) clonedEndPoints.toArray()[0];
        }
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
        return Integer.toString(length) + endPoints.toString();
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
    public boolean equals(Object object) {
        return super.equals(object); // REMOVE THIS LINE AND WRITE THIS METHOD
    }

    @Override
    public int hashCode() {
        return super.hashCode(); // REMOVE THIS LINE AND WRITE THIS METHOD
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

        if (length <= 0) {
            return false;
        }
        else if (endPoints == null) {
            return false;
        }
        else if (endPoints.isEmpty() || endPoints.contains(null)){
            return false;
        }
        else if (!(endPoints.size() == 2)) {
            return false;
        }

        Object[] endPointsArray = endPoints.toArray();

        if (endPointsArray[0].equals(endPointsArray[1])) {
            return false;
        }

        return true;
    }
}
