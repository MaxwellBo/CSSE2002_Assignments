package railway.gui;

import railway.FormatException;

import java.io.IOException;

/**
 * The controller for the Railway Manager.
 */
public class RailwayController {

    // the model that is being controlled
    private RailwayModel model;
    // the view that is being controlled
    private RailwayView view;

    // REMOVE THIS LINE AND DECLARE ANY ADDITIONAL VARIABLES YOU REQUIRE HERE

    /**
     * Initialises the Controller for the Railway Manager.
     */
    public RailwayController(RailwayModel model, RailwayView view) {
        this.model = model;
        this.view = view;

        loadTrack();
        addTrain();
    }

    public void loadTrack() {
        try {
            model.loadTrack("track.txt");
        }
        catch (Exception e) {
            view.makeDialogBox("File load error", e.toString());
        }
    }

    public void addTrain() {
        try {
            // TODO: Remove this boilerplate
            model.loadRoute("route0.txt", 1, 5);
        }
        catch (IOException | FormatException e) {
            view.makeDialogBox("File load error", e.toString());
        }
        catch (RuntimeException e) {
            view.makeDialogBox("Invalid route request", e.getMessage());
        }
    }
}
