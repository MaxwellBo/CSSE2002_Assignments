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

        loadTrack("track.txt");
        view.addLoadListener(new LoadActionListener());
        view.addViewListener(new ViewActionListener());
        view.addSetListener(new SetActionListener());
    }

    public void loadTrack(String filename) {
        try {
            model.loadTrack(filename);
        }
        catch (Exception e) {
            view.makeDialogBox("File load error", e.toString());
        }
    }

    private int parseStartOffset() {
        try {
            return Integer.parseInt(view.getStartOffsetFieldValue());
        }
        catch (NumberFormatException e) {
            view.makeDialogBox("Invalid start offset value",
                    "Start offset field must be an integer");
        }
        return -1; // TODO: Check if this is illegal
    }

    private int parseEndOffset() {
        try {
            return Integer.parseInt(view.getEndOffsetFieldValue());
        }
        catch (NumberFormatException e) {
            view.makeDialogBox("Invalid end offset value",
                   "End offset field must be an integer");
        }
        return -1;
    }

    private class LoadActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            String filename = view.getRouteFilenameFieldValue();
            int startOffset = parseStartOffset();
            int endOffset = parseEndOffset();

            try {
                int id = model.spawnTrain(filename, startOffset, endOffset);
                view.addListElement(Integer.toString(id));
            }
            catch (IOException | FormatException e) {
                view.makeDialogBox("Failed to load " + filename, e.toString());
            }
            catch (RuntimeException e) {
                view.makeDialogBox("Invalid route request", e.getMessage());
            }
        }
    }

    private class ViewActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                int selected = Integer.parseInt(view.getListSelectedValue());
                // TODO: Pipe to display screen
                System.out.println("ID: " + model.getTrainInfo(selected)[0]);
                System.out.println("Start offset: " + model.getTrainInfo(selected)[1]);
                System.out.println("End offset: " + model.getTrainInfo(selected)[2]);
                System.out.println("Route: \n " + model.getTrainInfo(selected)[3]);
            }
            catch (NumberFormatException e) {
                System.out.println(e.toString());
                view.makeDialogBox("No train selected", "Please select a train"
                        + " to view its information");
            }
        }
    }

    private class SetActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int startOffset = parseStartOffset();
            int endOffset = parseEndOffset();

            try {
                int selected = Integer.parseInt(view.getListSelectedValue());
                model.updateSubroute(selected, startOffset, endOffset);
            }
            catch (NumberFormatException e) {
                view.makeDialogBox("No train selected", "Please select a train"
                        + " to view its information");
            }
            catch (RuntimeException e) {
                view.makeDialogBox("Invalid route request", e.getMessage());
            }
        }
    }
}
