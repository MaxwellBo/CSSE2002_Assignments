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
     * An exception that is thrown to indicate an invalid train request.
     */
    public class InvalidTrainRequestException extends RuntimeException {

        public InvalidTrainRequestException() {
            super();
        }

        public InvalidTrainRequestException(String s) {
            super(s);
        }
    }

    /**
     * An exception that is thrown to indicate an invalid route request.
     */
    public class InvalidRouteRequestException extends RuntimeException {

        public InvalidRouteRequestException() {
            super();
        }

        public InvalidRouteRequestException(String s) {
            super(s);
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
            throws IOException, FormatException, InvalidRouteRequestException {

        // Throws for (i) the route could not be loaded
        Route route = RouteReader.read(filename);

        // Throws for (ii)
        if (!(route.onTrack(track))) {
            throw new InvalidRouteRequestException("The route was loaded,"
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
    public void updateSubroute(int id, int startOffset, int endOffset)
            throws InvalidTrainRequestException, InvalidRouteRequestException {
        try {
            setSubroute(trains.get(id), startOffset, endOffset);
        }
        catch (NumberFormatException e) {
            throw new InvalidRouteRequestException("The train requested to"
                    + " be updated does not exist");
        }
    }

    private int setSubroute(Train target, int startOffset, int endOffset)
            throws InvalidRouteRequestException {
        // Clear the train from a cloned model, if it exists
        Map<Integer, Train> trainsWORequested = new HashMap<>(trains);
        // When adding a new train, this call is redundant
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
        // When mutating train, this call is redundant
        trains.put(target.id, target);

        // Return the id / key of the train in the map
        return target.id;
    }

    private void verifyInterval(Route route, int startOffset, int endOffset)
            throws InvalidRouteRequestException {
        if (!(0 <= startOffset
                && startOffset < endOffset
                && endOffset <= route.getLength())) {
            throw new InvalidRouteRequestException("The route could be loaded and is"
                    + " on the track, but the offsets do not"
                    + " define a valid sub-route of the route that was read");
        }
    }

    private void verifyNoIntersections(Map<Integer, Train> trains, Route route)
            throws InvalidRouteRequestException {
        Predicate<Train> checkIntersectWithRoute = train ->
                train.subroute.intersects(route);

        if (trains.values()
                .stream()
                .anyMatch(checkIntersectWithRoute)) {
            throw new InvalidRouteRequestException("Requested subroute intersects with"
                    + " at least one of the sub-routes currently"
                    + " allocated to another train");
        }
    }
}
