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


    /**
     * Initialises the Controller for the Railway Manager.
     */
    public RailwayController(RailwayModel model, RailwayView view) {
        this.model = model;
        this.view = view;

        // Initialize the model
        // Exceptions handled
        loadTrack("track.txt");

        // Initialize the view
        // No exceptions
        view.addLoadListener(new LoadActionListener());
        view.addViewListener(new ViewActionListener());
        view.addSetListener(new SetActionListener());
    }

    /**
     * Using a file written in the format specified by TrackReader.read,
     * load the Track specified into the Model
     * @param filename
     *              the file to read from
     */
    public void loadTrack(String filename) {
        try {
            model.loadTrack(filename);
        }
        catch (Exception e) {
            view.makeDialogBox("File load error", e.toString());
        }
    }

    /**
     * Retrieves the string currently contained within the start offset
     * text field and converts it into an integer.
     *
     * This helper method exists so that the controller may be adapted to
     * defend against invalid input, currently handled by the view.
     *
     * @require the textfield to defend itself against invalid user input
     *              that typically causes parseInt to throw Exceptions
     * @return the value of the string
     */
    private int parseStartOffset() {
        return Integer.parseInt(view.getStartOffsetFieldValue());
    }

    /**
     * Retrieves the string currently contained within the end offset
     * text field and converts it into an integer.
     *
     * This helper method exists so that the controller may be adapted to
     * defend against invalid input, currently handled by the view.
     *
     * @require the textfield to defend itself against invalid user input
     *              that typically causes parseInt to throw Exceptions
     * @return the value of the string
     */
    private int parseEndOffset() {
        return Integer.parseInt(view.getEndOffsetFieldValue());
    }

    /**
     * The listener for the "New train" user action
     */
    private class LoadActionListener implements ActionListener {

        /**
         * Given a trigger, attempt to add a new train to the track,
         * updating the view and model accordingly
         *
         * @param event the trigger
         */
        public void actionPerformed(ActionEvent event) {

            String filename = view.getRouteFilenameFieldValue();

            // No exceptions
            int startOffset = parseStartOffset();
            int endOffset = parseEndOffset();

            try {
                int id = model.spawnTrain(filename, startOffset, endOffset);
                view.appendToList(Integer.toString(id));
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

    /**
     * The listener for the "view allocation" user action
     */
    private class ViewActionListener implements ActionListener {

        /**
         * Given a trigger, attempt to view the allocation of a particular
         * train contained within the model, and update the view accordingly
         *
         * @param event the trigger
         */
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
                view.makeDialogBox("No train selected", "Please select a train"
                        + " to view its information");
            }
        }
    }

    /**
     * The listener for the "update allocation" user action
     */
    private class SetActionListener implements ActionListener {

        /**
         * Given a trigger, attempt to update the allocation of a particular
         * train contained within the model, and update the model (and view)
         * accordingly
         *
         * @param event the trigger
         */
        public void actionPerformed(ActionEvent event) {

            int startOffset = parseStartOffset();
            int endOffset = parseEndOffset();

            try {
                int selected = Integer.parseInt(view.getListSelectedValue());
                model.setTrainSubroute(selected, startOffset, endOffset);
                view.clearFields();
            }
            catch (NumberFormatException e) {
                view.makeDialogBox("No train selected", "Please select a train"
                        + " to change its subroute");
            }
            catch (RailwayModel.InvalidRouteRequestException e) {
                view.makeDialogBox("Invalid route request", e.getMessage());
            }
        }
    }
}
