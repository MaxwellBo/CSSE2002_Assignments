package railway.gui;

import railway.FormatException;

import java.awt.*;
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

    // Button listeners
    private ActionListener loadActionListener;
    private ActionListener viewActionListener;
    private ActionListener setActionListener;

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
        // Bind these so that other listeners can call their actions
        loadActionListener = new LoadActionListener();
        viewActionListener = new ViewActionListener();
        setActionListener = new SetActionListener();

        view.addLoadListener(loadActionListener);
        view.addViewListener(viewActionListener);
        view.addSetListener(setActionListener);
    }

    /**
     * Using a file written in the format specified by TrackReader.read,
     * load the Track specified into the Model
     * @param filename
     *              the file to read from
     */
    public void loadTrack(String filename) {
        try {
            // throws IOException and FormatException
            model.loadTrack(filename);
        }
        catch (Exception e) {
            // toString provides the full diagnostic / stack trace
            // which may be useful for debugging IO errors
            // thus, Exception details are not hidden
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
            int startOffset = parseStartOffset();
            int endOffset = parseEndOffset();

            try {
                // throws invalid RouteRequestException
                int id = model.spawnTrain(filename, startOffset, endOffset);

                view.appendToList(Integer.toString(id));
                view.clearFields();
            }
            catch (IOException | FormatException e) {
                // toString provides the full diagnostic / stack trace
                // which may be useful for debugging IO errors
                // thus, Exception details are not hidden
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
                // throws NumberFormatException when the view returns null
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
                // throws NumberFormatException when the view returns null
                int selected = Integer.parseInt(view.getListSelectedValue());

                // throws InvalidRouteRequestException
                model.setTrainSubroute(selected, startOffset, endOffset);

                view.clearFields();
                viewActionListener.actionPerformed(event);
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
