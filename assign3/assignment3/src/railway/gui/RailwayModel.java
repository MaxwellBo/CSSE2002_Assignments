package railway.gui;

import railway.FormatException;
import railway.RouteReader;
import railway.TrackReader;
import railway.Track;
import railway.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * The model for the Railway Manager.
 */
public class RailwayModel {

    Track track;


    /**
     * Initialises the model for the Railway Manager.
     */
    public RailwayModel() {
    }

    public void loadTrack(String filename) throws IOException, FormatException {
        track = TrackReader.read(filename);
    }

    public void loadRoute(String filename, int startOffset, int endOffset)
            throws IOException, FormatException {

        // Throws for (i) the route could not be loaded
        Route route = RouteReader.read(filename);

        // (ii)
        if (!(route.onTrack(track)){
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
       if(false) {

       }
    }

}
