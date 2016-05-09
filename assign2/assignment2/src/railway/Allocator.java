package railway;

import java.util.*;
import java.util.function.Predicate;

public class Allocator {

    /**
     * This method takes as input a list of the routes that are currently
     * occupied by trains on the track, and a list of the routes requested by
     * each of those trains and returns an allocation of routes to trains based
     * on those inputs.
     * 
     * Such a method may be used by a train controller to manage the movement of
     * trains on the track so that they do not collide. (I.e. if a train has
     * been allocated to a route, it has permission to travel on that route.)
     * 
     * @require occupied != null && requested != null
     * 
     *          && !occupied.contains(null)
     * 
     *          && !requested.contains(null)
     * 
     *          && occupied.size() == requested.size()
     * 
     *          && none of the occupied routes intersect
     * 
     *          && the routes in the occupied list are non-empty, valid routes
     * 
     *          && the routes in the requested list are non-empty, valid routes
     * 
     *          && all the routes in occupied and requested are part of the same
     *          track.
     * 
     * @ensure Let N be the number of elements in the occupied list. This method
     *         returns a list of N routes, where, for each index i satisfying 0
     *         <= i < N, \result.get(i) is the route allocated to the ith train:
     *         the train currently occupying route occupied.get(i).
     * 
     *         The route allocated to the ith train is the longest prefix of
     *         requested.get(i) that does not intersect with any of the routes
     *         currently occupied by any other train, or any of the routes
     *         \result.get(j) for indices j satisfying 0 <= j < i. (I.e. trains
     *         with lower indices have higher priority.)
     * 
     *         Neither of the two input parameters, the occupied list and the
     *         requested list, are modified in any way by this method.
     *
     * @param occupied
     *            there are occupied.size() trains on the track, and parameter
     *            occupied is a list of the routes currently occupied by each of
     *            those trains. A precondition of this method is that none of
     *            the occupied routes are null or empty, they are valid routes,
     *            and that they do not intersect (i.e. no two trains can occupy
     *            the same location on the track at the same time).
     * @param requested
     *            a list of the routes requested by each of the occupied.size()
     *            trains. A precondition of the method is that occupied.size()
     *            == requested.size(), and that none of the requested routes are
     *            null or empty, and that they are valid routes. For index i
     *            satisfying 0 <= i < requested.size(), requested.get(i) is the
     *            route requested by the train currently occupying the route
     *            occupied.get(i).
     * @return the list of allocated routes.
     */
    public static List<List<Segment>> allocate(List<List<Segment>> occupied,
            List<List<Segment>> requested) {

        // Create an empty list to contain the allocated routes
        List<List<Segment>> collector = new ArrayList<>();

        // Early return empty list if no trains exist
        if (occupied.isEmpty() || requested.isEmpty()) {
            return collector;
        }

        // Select the requested route's index that we want to verify
        for (int train = 0; train < requested.size(); train++) {
            // Clone the route we're attempting to verify,
            // so that we can mutate it to comply with the other routes
            List<Segment> staged = new ArrayList<>(requested.get(train));

            // Create a clone of the occupied routes ...
            List<List<Segment>> occupiedWOTrain = new ArrayList<>(occupied);
            // ... and yank the train's old route that we're trying to move
            occupiedWOTrain.remove(train);

            // Bind a function that checks if a given route (List<Segment)
            // intersects with the staged route, and returns a boolean
            // as to whether it does
            Predicate<List<Segment>> checkRouteIntersectionWStaged =
                    route -> {
                        for (Segment segA : route) {
                            for (Segment segB : staged) {
                                // Compare every Segment in route
                                // against every Segment staged
                                // and check if the segments overlap

                                if (segA.contains(segB.getFirstLocation())
                                        || segA.contains(segB.getLastLocation())
                                        || segB.contains(segA.getFirstLocation())
                                        || segB.contains(segA.getLastLocation())
                                        ) {

                                    // If they do, the routes intersect
                                    return true;
                                }
                            }
                        }
                        return false;
                    };

            // Repeatedly shorten the route until no intersections
            while (true) {
                // "[must] not intersect with any of the routes
                // currently occupied by *any other* train"
                // Map the comparison function across the collection
                // of routes. Returns true if any intersect
                boolean intersectOccupied = occupiedWOTrain
                        .stream()
                        .anyMatch(checkRouteIntersectionWStaged);

                // "or any of the routes [in the result]"
                boolean intersectCollector = collector
                        .stream()
                        .anyMatch(checkRouteIntersectionWStaged);

                // Do we need to shorten the route?
                if (!(intersectOccupied || intersectCollector)) {
                    // No intersections
                    // No need to shorten the route
                    // Escape the "shortening loop"
                    break;
                }

                // If we need to shorten the route, pop off a Segment...
                Segment last = staged.remove(staged.size() - 1);

                // Check if the Segment can't be reduced in size
                // Segment invariant: "startOffSet < endOffset"
                if ((last.getStartOffset()) >= (last.getEndOffset() - 1)) {
                    // if it can't be reduced in size,
                    // leave the Segment popped off
                    continue;
                }

                // If it can, shorten it...
                Segment toAdd = new Segment(
                        last.getSection()
                        , last.getDepartingEndPoint()
                        , last.getStartOffset()
                        , last.getEndOffset() - 1);

                // ... and add the Segment back ...
                staged.add(toAdd);
                // Loop jump
            }
            // Staged route finalized
            // Add the staged route to be allocated
            collector.add(staged);
        }
        return collector;
    }
}
