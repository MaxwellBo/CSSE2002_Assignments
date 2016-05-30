package railway.gui;

import railway.FormatException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
        view.addViewListener(new ViewActionListener());
    }

    public void loadTrack() {
        try {
            model.loadTrack("track.txt");
            System.out.println("FILE LOAD SUCCESS");
        }
        catch (Exception e) {
            view.makeDialogBox("File load error", e.toString());
        }
    }

    public void addTrain() {
        try {
            // TODO: Remove this boilerplate
            model.spawnTrain("route0.txt", 0, 22);
            System.out.println("ROUTE LOAD SUCCESS");
            model.spawnTrain("route1.txt", 0, 22);
            System.out.println("ROUTE LOAD SUCCESS");
            model.updateSubroute(0, 0, 17);
        }
        catch (IOException | FormatException e) {
            view.makeDialogBox("File load error", e.toString());
        }
        catch (RuntimeException e) {
            view.makeDialogBox("Invalid route request", e.getMessage());
        }
    }

    private class ViewActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selected = view.getListSelectedIndex();

            // TODO: Pipe to display screen
            System.out.println(model.getTrainInfo(selected)[3]);
        }
    }
}
