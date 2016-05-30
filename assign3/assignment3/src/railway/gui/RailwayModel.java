package railway.gui;

import railway.FormatException;
import railway.RouteReader;
import railway.TrackReader;
import railway.Track;
import railway.Route;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The model for the Railway Manager.
 */
public class RailwayModel {

    private Track track;
    private final Map<Integer, Train> trains;

    private class Train {

        final int id;
        final Route route;
        int startOffset;
        int endOffset;
        Route subroute;

        Train(int id, Route route) {
            this.id = id;
            this.route = route;
        }

        void setSubroute(int startOffset, int endOffset) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.subroute = route.getSubroute(startOffset, endOffset);
        }
    }


    /**
     * Initialises the model for the Railway Manager.
     */
    public RailwayModel() {
        this.trains = new HashMap<>();
    }

    public void loadTrack(String filename) throws IOException, FormatException {
        track = TrackReader.read(filename);
    }

    public int spawnTrain(String filename, int startOffset, int endOffset)
            throws IOException, FormatException {

        // Throws for (i) the route could not be loaded
        Route route = RouteReader.read(filename);

        // Throws for (ii)
        if (!(route.onTrack(track))) {
            throw new RuntimeException("The route was loaded,"
                    + " but it is not on the train management system’s track");
        }

        Train spawned = new Train(trains.size(), route);

        // Continue with building the rest of the train's attributes
        return setSubroute(spawned, startOffset, endOffset);

    }

    public Set<Integer> getTrainIDs() {
        return trains.keySet();
    }

    // precond not null
    // precond train exists
    // --> requested becomes null, checks fail
    public String[] getTrainInfo(int id) {
        Train requested = trains.get(id);
        String[] info = { Integer.toString(requested.id)
                , Integer.toString(requested.startOffset)
                , Integer.toString(requested.endOffset)
                , requested.route.toString() };
        return info;
    }

    // TODO: Change name to updateTrainSubroute
    public void updateSubroute(int id, int startOffset, int endOffset) {
        setSubroute(trains.get(id), startOffset, endOffset);
    }

    private int setSubroute(Train target, int startOffset, int endOffset) {
        // Clear the train from a cloned model, if it exists
        Map<Integer, Train> trainsWORequested = new HashMap<>(trains);
        trainsWORequested.remove(target.id);

        // Check that the subroute can exist
        // Throws for (iii)
        verifyInterval(target.route, startOffset, endOffset);

        // Stage the subroute
        Route requested = target.route.getSubroute(startOffset, endOffset);

        // Check that the subroute is compatible
        // Throws for (iv)
        verifyNoIntersections(trainsWORequested, requested);

        // Mutate the train so that it has the new subroute
        target.setSubroute(startOffset, endOffset);

        // Either bind or overwrite the target into the real model
        trains.put(target.id, target);

        // Return the id / key of the train in the map
        return target.id;
    }

    private void verifyInterval(Route route, int startOffset, int endOffset) {
        if (!(0 <= startOffset
                && startOffset < endOffset
                && endOffset <= route.getLength())) {
            throw new RuntimeException("The route could be loaded and is"
                    + " on the track, but the offsets do not"
                    + " define a valid sub-route of the route that was read");
        }
    }

    private void verifyNoIntersections(Map<Integer, Train> trains, Route route) {
        Predicate<Train> checkIntersectWithRoute = train ->
                train.subroute.intersects(route);

        if (trains.values()
                .stream()
                .anyMatch(checkIntersectWithRoute)) {
            throw new RuntimeException("Requested subroute intersects with"
                    + " at least one of the sub-routes currently"
                    + " allocated to another train");
        }
    }
}
