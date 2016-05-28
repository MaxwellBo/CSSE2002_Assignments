package railway.gui;

import railway.FormatException;
import railway.TrackReader;
import railway.Track;

import java.io.IOException;

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
}
