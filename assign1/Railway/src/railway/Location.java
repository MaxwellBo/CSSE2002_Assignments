package railway;

/**
 * <p>
 * An immutable class representing a location on a railway track.
 * </p>
 * 
 * <p>
 * A location on a railway track may be at either at a junction in the track, or
 * on a section of the track between the two end-points of the section.
 * </p>
 * 
 * <p>
 * Note: A location at a junction lies on all of the sections of track that are
 * connected to the junction; a location that is not at a junction, lies on only
 * one section of track.
 * </p>
 * 
 * <p>
 * A location can be identified by its offset with respect to an end-point of a
 * section of the track. However, the same location can be identified by
 * different descriptions. For example, given a section s of length 10 and with
 * end-points (j1, FACING) and (j2, REVERSE), the same location may equivalently
 * be described as being either 3 meters from junction j1 along its FACING
 * branch, or 7 meters from junction j2 along its REVERSE branch. A location
 * which is to be found at a junction, is represented as having a 0 offset from
 * the junction along any branch of the junction. For this reason, the
 * equivalence method of this class is more complex than usual.
 * </p>
 */
public class Location {

    /**
     * The section on which the Location is defined against.
     */
    private final Section section;

    /**
     * The end-point, on which the Location is defined, via its offset
     * from the end-point along the section.
     */
    private final JunctionBranch endPoint;

    /**
     * The offset from the end-point, along the section, to the Location.
     */
    private final int offset;

    /*
     * invariant: section != null
     *
     *         && endPoint != null
     *
     *         && offset >= 0
     *
     *         && offset < section.getLength()
     *
     *         && section.getEndPoints.contains(endPoint)
     *         
     *         && section.checkInvariant()
     *
     *         && the section and endPoint maintain
     *            their post-conditions and invariants
     */

    /**
     * Creates a new location that lies on the given section at a distance of
     * offset meters from endPoint.getJunction() along endPoint.getBranch().
     * 
     * @param section
     *            a section that the location lies on
     * @param endPoint
     *            an end-point of the given section
     * @param offset
     *            the distance of the location from the given end-point of the
     *            section that it lies on. The distance must be greater than or
     *            equal to zero, and strictly less than the length of the
     *            section.
     * @throws NullPointerException
     *             if either parameter section or endPoint is null.
     * @throws IllegalArgumentException
     *             if (i) offset is either a negative value or if it is greater
     *             than or equal to the length of the given section, or (ii)
     *             section and endPoint are not null, but parameter endPoint is
     *             not equivalent to an end-point of the given section.
     */
    public Location(Section section, JunctionBranch endPoint, int offset) {
        if (section == null) {
            throw new NullPointerException("section parameter is null");
        }
        else if (endPoint == null) {
            throw new NullPointerException("endPoint parameter is null");
        }
        else if (offset < 0) {
            throw new IllegalArgumentException("offset parameter"
                    + " is less than zero");
        }
        else if (offset >= section.getLength()) {
            throw new IllegalArgumentException("offset from the start point"
                    + " is greater than or equal to the length"
                    + " of the section parameter's length");
        }
        else if (!(section.getEndPoints().contains(endPoint))) {
            throw new IllegalArgumentException("section parameter"
                    + " does not contain endPoint parameter");
        }
        else {
            this.section = section;
            this.endPoint = endPoint;
            this.offset = offset;
        }
    }

    /**
     * <p>
     * Returns a section of the track that this location lies on. Note that a
     * location at a junction may lie on multiple sections, and this method only
     * returns one of them: the section that this method was constructed with.
     * </p>
     * 
     * <p>
     * Note: locations that are equivalent according to the equals method of this
     * class may have different return-values of this method.
     * </p>
     * 
     * @return a section that this location lies on
     */
    public Section getSection() {
        return section;
    }

    /**
     * <p>
     * This method returns an end-point of the section this.getSection(), that
     * this location lies on. The end-point that is returned is the one that
     * this class was constructed with.
     * </p>
     *
     * <p>
     * Note: locations that are equivalent according to the equals method of this
     * class may have different return-values of this method.
     * </p>
     * 
     * @return an end-point of this.getSection()
     */
    public JunctionBranch getEndPoint() {
        return endPoint;
    }

    /**
     * <p>
     * This method returns the offset of this location with respect to the
     * end-point this.getEndPoint() that lies on the track section
     * this.getSection().
     * </p>
     * 
     * <p>
     * Equivalent locations can be equivalently described by different offsets
     * and end-points. Methods getOffset() and getEndPoint() return the offset
     * and end-point that this location was constructed with.
     * </p>
     * 
     * <p>
     * Note: locations that are equivalent according to the equals method of this
     * class may have different return-values of this method.
     * </p>
     * 
     * @return the offset of this location with respect to this.getEndPoint()
     * 
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Returns true if this location is at a junction (i.e. it has an offset of
     * zero with respect to the end-point of a section).
     * 
     * @return whether or not this location is at a junction
     */
    public boolean atAJunction() {
        return (getOffset() == 0);
    }

    /**
     * <p>
     * Returns true if this location lies on the given section.
     * </p>
     * 
     * <p>
     * This location lies on a section if and only if either (i) it is at a
     * junction on an end-point of the given section or (ii) it is not at a
     * junction, and the given section is equivalent to this.getSection().
     * </p>
     * 
     * @param section
     *            the section to check
     * @return true iff this location lies on the given section
     */
    public boolean onSection(Section section) {
        // Case (i)
        // "at a junction..."
        if (atAJunction()) {
             // for easy comparison,
             // save the Junction on which the Location resides as...
            Junction thisJunction = getEndPoint().getJunction();

            for (JunctionBranch i : section.getEndPoints()) {
                // "... on an end-point of the given section"
                if (thisJunction.equals(i.getJunction())) {
                    return true; // early return
                }
            }
            // fallthrough to
            return false;
        }
        // Case (ii)
        // "not at a junction..."
        else {
            // "... and the given section is equivalent to this.getSection().
            return getSection().equals(section);
        }
    }

    /**
     * <p>
     * If this location is at a junction (i.e. it has an offset of zero from the
     * end-point of a section), then this method returns the toString()
     * representation of the junction where this location lies.
     * 
     * Otherwise, if the location is not at a junction, this method returns a
     * string of the form: <br>
     * <br>
     * 
     * "Distance OFFSET from JUNCTION along the BRANCH branch" <br>
     * <br>
     * 
     * where OFFSET is this.getOffset(), JUNCTION is the toString()
     * representation of the junction of this.getEndPoint(), and BRANCH is the
     * toString() representation of the branch of this.getEndPoint().
     * </p>
     * 
     * <p>
     * Note: locations that are equivalent according to the equals method of this
     * class may have different return-values of this method.
     * </p>
     */
    @Override
    public String toString() {
        if (atAJunction()) {
            return getEndPoint().getJunction().toString();
        }
        else {
            return "Distance " + Integer.toString(getOffset()) +
                    " from " + getEndPoint().getJunction().toString() +
                    " along the " + getEndPoint().getBranch().toString() +
                    " branch";
        }
    }

    /**
     * <p>
     * Two locations are equivalent if either: <br>
     * <br>
     *
     * (i) their offsets are both zero and their end-points are at the same
     * junction (two junctions are considered to be the same if they are
     * equivalent according to the equals method of the Junction class) or <br>
     * <br>
     *
     * (ii) if their end-points are equivalent and their offsets are equal, or <br>
     * <br>
     *
     * (iii) if their end-points are not equivalent, but they lie on the same
     * section, and the sum of the length of their offsets equals the length of
     * the section that they lie on. (Two sections are considered to be the same
     * if they are equal according to the equals method of the Section class.) <br>
     * <br>
     *
     * and they are not equivalent otherwise.
     * </p>
     *
     * <p>
     * This method returns true if and only if the given object is an instance
     * of the class Location, and the locations are equivalent according to the
     * above definition.
     * </p>
     */
    @Override
    public boolean equals(Object o) {
        // check reference equality
        if (this == o) {
            return true;
        }

        // verify that o can be coerced without throwing errors
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // coerce the Object into a Location
        Location location = (Location) o;

        if (atAJunction()
                && location.atAJunction()
                && getEndPoint().getJunction().equals(
                   (location.getEndPoint().getJunction()))) {
            // Case (i)
            // if offsets are both zero -> both atAJunction()
            return true;
        }
        else if (getEndPoint().equals(location.getEndPoint())
                && getOffset() == location.getOffset()) {
            // Case (ii)
            return true;
        }
        else if (getSection().equals(location.getSection())
                && (getOffset() + location.getOffset()
                        == getSection().getLength())) {
            // Case (iii)
            // end-points are not equivalent as Case (ii) failed
            return true;
        }
        else {
            // all equality checks failed
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (atAJunction()) {
            // use the Junction's hashcode
            return getEndPoint().getJunction().hashCode();
        }
        else {
            // start with section's hashcode as the collector
            int result = section.hashCode();

            if (offset < section.getLength() / 2.0) { // to prevent flooring
                // introduce both the end-point hashcode and the offset
                result = 31 * result + endPoint.hashCode();
                result = 31 * result + offset;
            }
            else if (offset == section.getLength() / 2.0) {
                // do nothing, and let only the section hashcode be returned
            }
            else if (offset > section.getLength() / 2.0) {
                // via section this Location resides on, fetch the other
                // end-point, invert the offset, and introduce them
                result = 31 * result
                        + section.otherEndPoint(endPoint).hashCode();
                result = 31 * result + (section.getLength() - offset);
            }
            return result; // return collector
        }
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
        // shortcircuit on invariant violation
        return !(section == null
                || endPoint == null
                || offset < 0
                || offset >= section.getLength()
                || !(section.getEndPoints().contains(endPoint))
                || !(section.checkInvariant())
                || !(endPoint.checkInvariant())
                );
        // no further checks
    }
}

