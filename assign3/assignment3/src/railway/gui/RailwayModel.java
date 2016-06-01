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

    // The Track that the model is simulating
    private Track track;
    // The Trains on the track and their unique IDs
    private final Map<Integer, Train> trains;

    /**
     * A struct-like Train datatype with a single setter to ensure internal
     * consistency. The RailwayModel has the responsibility to maintain
     * the integrity of instances of this datatype, as some of
     * its fields are public and mutable.
     */
    private class Train {

        // the Train's unique ID
        final int id;
        // the Route that the train is assigned to
        final Route route;
        // the start offset of the subroute
        int startOffset;
        // the end offset of the subroute
        int endOffset;
        // the Train's currently assigned subroute
        Route subroute;

        /**
         * Creates a new Train running on a route
         *
         * @param id the ID to assign this train
         * @param route the route on which this Train runs
         */
        Train(int id, Route route) {
            this.id = id;
            this.route = route;
        }

        /**
         * Sets the subroute of this train with two offset lengths
         * using this Train's internal route attribute
         *
         * @param startOffset the start offset of the subroute
         * @param endOffset the end offset of the subroute
         */
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

    /**
     * Using a file written in the format specified by TrackReader.read,
     * load the returned Track into this RailwayModel for use by trains.
     *
     * @param filename
     *            the file to read from
     * @throws IOException
     *             if there is an error reading from the input file
     * @throws FormatException
     *             if there is an error with the input format. The
     *             FormatExceptions thrown should have a meaningful message that
     *             accurately describes the problem with the input file format,
     *             including the line of the file where the problem was
     *             detected.
     */
    public void loadTrack(String filename) throws IOException, FormatException {
        this.track = TrackReader.read(filename);
    }

    /**
     * Spawns a new Train in the model, sourcing its route from a specified
     * file, and assigning it a subroute using a start offset and an end offset
     *
     * @param filename
     *            the file to read from
     * @param startOffset
     *            the start offset of the new Train's subroute
     * @param endOffset
     *            the end offset of the new Train's subroute
     * @require the filename is not null
     * @return the ID of the Train that was spawned in the model
     * @throws IOException
     *              if there is an error reading from the input file
     * @throws FormatException
     *              if there is an error with the input format.
     * @throws InvalidRouteRequestException
     *              if the route could not be loaded
     *              if the route could be loaded,
     *                  but it is not on the train management system’s track
     *              if the offsets do not define a
     *                  valid sub-route of the route that was read
     *              if the sub-route is valid w.r.t. the train’s route,
     *                  but the sub-route route.getSubroute(startOffset, endOffset)
     *                  intersects with at least one of the sub-routes
     *                  currently allocated to another train
     */
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

    /**
     * Returns the ID, start offset, end offset and full route of
     * a train in String format
     *
     * @param id the ID of the Train to retrive the information of
     * @require the id parameter is not null and a train with the specified
     *              id exists within the model
     * @return the information of the Train in a array of length 4
     */
    public String[] getTrainInfo(int id) {
        Train requested = trains.get(id);

        String[] info = { Integer.toString(requested.id)
                , Integer.toString(requested.startOffset)
                , Integer.toString(requested.endOffset)
                , requested.route.toString() };

        return info;
    }

    /**
     *
     * @param id
     * @param startOffset
     * @param endOffset
     * @throws InvalidTrainRequestException
     * @throws InvalidRouteRequestException
     */
    public void setTrainSubroute(int id, int startOffset, int endOffset)
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
