package railway.gui;

import railway.FormatException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.NumberFormat;

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
//        addTrain();
        view.addLoadListener(new LoadActionListener());
        view.addViewListener(new ViewActionListener());
        view.addSetListener(new SetActionListener());
    }

    public void loadTrack(String filename) {
        try {
            model.loadTrack(filename);

            // TODO: Remove
            System.out.println(filename + " loaded successfully");
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

    private class LoadActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            String filename = "route0.txt";
            int startOffset = 0;
            int endOffset = 22;

            try {
                int id = model.spawnTrain(filename, startOffset, endOffset);
                view.addListElement(Integer.toString(id));

                // TODO: Remove
                System.out.println(filename + " loaded successfully");
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
            catch (Exception e) {
                System.out.println(e.toString());
                view.makeDialogBox("No train selected", "Please select a train"
                        + " to view its information");
            }
        }
    }

    private class SetActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int selected = -1;

            try {
                selected = Integer.parseInt(view.getListSelectedValue());
            }
            catch (NumberFormatException e) {
                view.makeDialogBox("No train selected", "Please select a train"
                        + " to view its information");
            }

            try {
                int startOffset = Integer.parseInt(view.getEndOffsetFieldValue());
                int endOffset = Integer.parseInt(view.getEndOffsetFieldValue());
                model.updateSubroute(selected, startOffset, endOffset);
            }
            catch (NumberFormatException e) {
                view.makeDialogBox("Invalid field input", e.getMessage());
            }
            catch (RuntimeException e) {
                view.makeDialogBox("Invalid route request", e.getMessage());
            }
        }
    }
}
