package railway.gui;

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
    }

    public void loadTrack() {
        try {
            model.loadTrack("track.txt");
        }
        catch (Exception e) {
            view.makeDialogBox("track.txt could not be loaded", e.toString());
        }
    }

    public void addTrain() {
        try {
            // TODO: Remove this boilerplate
            model.loadRoute("route0", 1, 5);
        }
        catch (Exception e) {

        }
    }
}
