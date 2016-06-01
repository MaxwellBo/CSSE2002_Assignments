package railway.gui;

import railway.FormatException;
import railway.RouteReader;
import railway.TrackReader;
import railway.Track;
import railway.Route;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The model for the Railway Manager.
 */
public class RailwayModel {

    private Track track;
    private final Map<Integer, Train> trains;


    /**
     * A struct-like Train datatype with a single setter to ensure internal
     * consistency.
     */
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

        // Throws IOException and FormatException
        Route route = RouteReader.read(filename);

        if (!(route.onTrack(track))) {
            throw new InvalidRouteRequestException("The route was loaded,"
                    + " but it is not on the train management system’s track");
        }

        Train spawned = new Train(trains.size(), route);

        // Continue with building the rest of the train's attributes
        // Throws InvalidRouteRequestException
        // Returns the ID of the train that was built
        return setSubroute(spawned, startOffset, endOffset);

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

    // TODO: Change name to setTrainSubroute
    public void updateSubroute(int id, int startOffset, int endOffset)
            throws InvalidTrainRequestException, InvalidRouteRequestException {
        try {
            setSubroute(trains.get(id), startOffset, endOffset);
        }
        catch (NumberFormatException e) {
            throw new InvalidTrainRequestException("The train requested to"
                    + " be updated does not exist");
        }
    }

    private int setSubroute(Train target, int startOffset, int endOffset)
            throws InvalidRouteRequestException {
        // Clear the train from a cloned model, if it exists
        Map<Integer, Train> trainsWORequested = new HashMap<>(trains);

        // When adding a new train, this call is redundant
        // Fails silently and without harm
        trainsWORequested.remove(target.id);

        // Verify the validity of the subroute
        try {
            // Throws IllegalArgumentException
            Route requested = target.route.getSubroute(startOffset, endOffset);

            // Throws InvalidRouteRequestException
            verifyNoIntersections(trainsWORequested, requested);
        }
        catch (IllegalArgumentException e) {
            throw new InvalidRouteRequestException("The route could be"
                    + " loaded and is on the track, but the offsets do not"
                    + " define a valid sub-route of the route that was read");
        }
        catch (InvalidRouteRequestException e) {
            throw e;
        }

        // Mutate the train so that it has the new subroute
        target.setSubroute(startOffset, endOffset);

        // Either bind or overwrite the target into the real model
        // When mutating train, this call is redundant
        // Fails silenty and without harm
        trains.put(target.id, target);

        // Return the ID / key of the train in the map
        return target.id;
    }

    private void verifyNoIntersections(Map<Integer, Train> trains
            , Route subroute) throws InvalidRouteRequestException {
        Predicate<Train> checkIntersectWithRoute = train ->
                train.subroute.intersects(subroute);

        if (trains.values()
                .stream()
                .anyMatch(checkIntersectWithRoute)) {
            throw new InvalidRouteRequestException("Requested subroute"
                    + " intersects with"
                    + " at least one of the sub-routes currently"
                    + " allocated to another train");
        }
    }
}
