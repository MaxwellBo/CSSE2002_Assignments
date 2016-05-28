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
    private Map<Integer, Train> trains;

    private class Train {

        final int id;
        final int startOffset;
        final int endOffset;
        final Route subroute;
        final Route route;

        public Train(int id, int startOffset, int endOffSet, Route route) {
            this.id = id;
            this.startOffset = startOffset;
            this.endOffset = endOffSet;
            this.subroute = route.getSubroute(startOffset, endOffSet);
            this.route = route;
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

    public void loadRoute(String filename, int startOffset, int endOffset)
            throws IOException, FormatException {

        // Throws for (i) the route could not be loaded
        Route route = RouteReader.read(filename);

        // (ii)
        if (!(route.onTrack(track))) {
            throw new RuntimeException("The route was loaded,"
                    + " but it is not on the train management system’s track");
        }

        // (iii)
        if (!(0 <= startOffset
                && startOffset < endOffset
                && endOffset <= route.getLength())) {
            throw new RuntimeException("The route could be loaded and is"
                    + " on the track, but the offsets do not"
                    + " define a valid sub-route of the route that was read");
        }

        // (iv)
        // Checks if a train intersects with the captured requested route
        Predicate<Train> checkIntersectWithRoute = train -> {
            // Capture here
            Route requested = route.getSubroute(startOffset, endOffset);
            return train.subroute.intersects(requested);
        };

        if (trains.values()
                .stream()
                .anyMatch(checkIntersectWithRoute)) {
            throw new RuntimeException("Requested subroute intersects with"
                    + " at least one of the sub-routes currently"
                    + " allocated to another train");
        }

        // all checks finished
        // fallthrough to
        Train toAdd = new Train(trains.size(), startOffset, endOffset, route);
        trains.put(trains.size(), toAdd);
    }
}
