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
        return Integer.parseInt(view.getStartOffsetFieldValue());
    }

    private int parseEndOffset() {
        return Integer.parseInt(view.getEndOffsetFieldValue());
    }

    private class LoadActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            String filename = view.getRouteFilenameFieldValue();
            int startOffset = parseStartOffset();
            int endOffset = parseEndOffset();

            try {
                int id = model.spawnTrain(filename, startOffset, endOffset);
                view.addListElement(Integer.toString(id));
                view.clearFields();
            }
            catch (IOException | FormatException e) {
                view.makeDialogBox("Failed to load " + filename, e.toString());
            }
            catch (RailwayModel.InvalidRouteRequestException e) {
                view.makeDialogBox("Invalid route request", e.getMessage());
            }
        }
    }

    private class ViewActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                int selected = Integer.parseInt(view.getListSelectedValue());

                String newline = System.getProperty("line.separator");

                view.clearDisplay();
                view.appendToDisplay("ID: "
                        + model.getTrainInfo(selected)[0]
                        + newline);
                view.appendToDisplay("Start offset: "
                        + model.getTrainInfo(selected)[1]
                        + newline);
                view.appendToDisplay("End offset: "
                        + model.getTrainInfo(selected)[2]
                        + newline);
                view.appendToDisplay("Route: "
                        + model.getTrainInfo(selected)[3]
                        + newline);
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
                // Will always succeed, as the entry box defends against
                // invalid data being input
                int selected = Integer.parseInt(view.getListSelectedValue());
                model.updateSubroute(selected, startOffset, endOffset);
                view.clearFields();
            }
            catch (NumberFormatException e) {
                view.makeDialogBox("No train selected", "Please select a train"
                        + " to view its information");
            }
            catch (RailwayModel.InvalidRouteRequestException e) {
                view.makeDialogBox("Invalid route request", e.getMessage());
            }
        }
    }
}
