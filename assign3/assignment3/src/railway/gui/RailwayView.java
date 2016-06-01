package railway.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * The view for the Railway Manager.
 */
@SuppressWarnings("serial")
public class RailwayView extends JFrame {

    // the model of the Railway Manager
    private RailwayModel model;
    private JButton loadButton;
    private JButton viewButton;
    private JButton setButton;
    private JComboBox<String> trainIDs;
    private JTextField routeFilenameField;
    private JFormattedTextField startOffsetField;
    private JFormattedTextField endOffsetField;
    private JTextArea display;

    /**
     * Creates a new Railway Manager window.
     */
    public RailwayView(RailwayModel model) {
        this.model = model;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Train Management System - 43926871");
        setBounds(400, 400, 800, 500);
        setResizable(false);

        Container container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));

        addButtons(container);
        addFields(container);
        addList(container);
        addDisplay(container);
    }

    /**
     * Given a title string and a message string, pop up a dialog box
     * with "Error" styling
     *
     * @param eClass
     *              the title of the dialog box
     * @param eMessage
     *              the text contained within the dialog box
     */
    public void makeDialogBox(String eClass, String eMessage) {
        JOptionPane.showMessageDialog(
                this
                , eMessage
                , eClass
                , JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Returns the currently selected string in the dropdown box
     *
     * @return the currently selected string in the dropdown bo
     */
    public String getListSelectedValue() {
        return (String)trainIDs.getSelectedItem();
    }

    /**
     * Add a new string to the dropdown box
     *
     * @param elem
     *              the string to be added to the dropdown box
     */
    public void appendToList(String elem) {
        trainIDs.addItem(elem);
    }

    /**
     * Returns the string currently contained within the start offset field
     *
     * @return the string currently contained within the start offset field
     */
    public String getStartOffsetFieldValue() {
        return startOffsetField.getText();
    }

    /**
     * Returns the string currently contained within the end offset field
     *
     * @return the string currently contained within the end offset field
     */
    public String getEndOffsetFieldValue() {
        return endOffsetField.getText();
    }

    /**
     * Returns the string currently contained within the route filename field
     *
     * @return the string currently contained within the route filename field
     */
    public String getRouteFilenameFieldValue() {
        return routeFilenameField.getText();
    }

    /**
     * Clears all fields and adds "default" values in their place
     */
    public void clearFields() {
        startOffsetField.setText("0");
        endOffsetField.setText("0");
        routeFilenameField.setText("");
    }

    /**
     * Appends the specified text to the display
     *
     * @param text the String to be added
     */
    public void appendToDisplay(String text) {
        display.append(text);
    }

    /**
     * Removes all text from the display
     */
    public void clearDisplay() {
        display.setText("");
    }

    /**
     * Bind an ActionListener to the "New" button
     *
     * @param listener
     *              the listener to be bound
     */
    public void addLoadListener(ActionListener listener) {
        loadButton.addActionListener(listener);
    }

    /**
     * Bind an ActionListener to the dropdown box
     *
     * @param listener
     *              the listener to be bound
     */
    public void addViewListener(ActionListener listener) {
        // viewButton.addActionListener(listener)
        trainIDs.addActionListener(listener);
    }

    /**
     * Bind an ActionListener to the "Update" button
     *
     * @param listener
     *              the listener to be bound
     */
    public void addSetListener(ActionListener listener) {
        setButton.addActionListener(listener);
    }

    /**
     * Given a container, initialize the "New", "View" and "Update" buttons,
     * and add them to the container
     *
     * @param container
     *              the container in which to put the buttons and their labels
     *                  into
     */
    private void addButtons(Container container) {
        JPanel panel = new JPanel(new GridLayout(1, 3));

        // Grandfathered variable names, apologies
        loadButton = new JButton("New");
        viewButton = new JButton("View");
        setButton = new JButton("Update");

        panel.add(loadButton);
        // panel.add(viewButton);
        // Last minute change
        panel.add(setButton);

        container.add(panel);
    }

    /**
     * Given a container, initialize the route filename field,
     * start offset field, end offset field and their
     * respective labels and add them to the container
     *
     * @param container
     *              the container put the buttons and their labels
     *                  into
     */
    private void addFields(Container container) {
        JPanel panel = new JPanel();

        // Defend against invalid user input
        NumberFormat onlyNumbers = NumberFormat.getNumberInstance();
        onlyNumbers.setGroupingUsed(false);

        JLabel routeFilenameFieldLabel = new JLabel(" Route filename");
        routeFilenameField = new JTextField(15);

        JLabel startOffsetFieldLabel = new JLabel(" Start offset");
        startOffsetField = new JFormattedTextField(onlyNumbers);
        startOffsetField.setValue(new Double(0));
        startOffsetField.setColumns(7);

        JLabel endOffsetFieldLabel = new JLabel(" End offset");
        endOffsetField = new JFormattedTextField(onlyNumbers);
        endOffsetField.setValue(new Double(0));
        endOffsetField.setColumns(7);

        panel.add(routeFilenameFieldLabel);
        panel.add(routeFilenameField);
        panel.add(startOffsetFieldLabel);
        panel.add(startOffsetField);
        panel.add(endOffsetFieldLabel);
        panel.add(endOffsetField);

        container.add(panel);
    }

    /**
     * Given a container, initialize the dropdown box, and add it to the
     * container
     *
     * @param container
     *              the container to put the dropdown box into
     */
    private void addList(Container container) {
        JPanel panel = new JPanel();

        trainIDs = new JComboBox<>();
        panel.add(trainIDs);

        container.add(panel);
    }

    /**
     * Given a container, initialize the display / console area, bind its
     * scrollbars, and add the union to the container
     *
     * @param container
     *              the container to put the display into
     */
    private void addDisplay(Container container) {
        JPanel panel = new JPanel();

        display = new JTextArea(15, 50);
        display.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(display);
        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollPane);

        container.add(panel);
    }
}
