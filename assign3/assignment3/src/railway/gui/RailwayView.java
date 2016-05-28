package railway.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The view for the Railway Manager.
 */
@SuppressWarnings("serial")
public class RailwayView extends JFrame {

    // the model of the Railway Manager
    private RailwayModel model;

    // REMOVE THIS LINE AND DECLARE ANY ADDITIONAL VARIABLES YOU REQUIRE HERE

    /**
     * Creates a new Railway Manager window.
     */
    public RailwayView(RailwayModel model) {
        this.model = model;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // REMOVE THIS LINE AND COMPLETE THIS METHOD
    }

    // REMOVE THIS LINE AND ADD YOUR OWN METHODS ETC HERE
}
