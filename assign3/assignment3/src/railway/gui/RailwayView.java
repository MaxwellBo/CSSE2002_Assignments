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
    private JList<String> list;
    private DefaultListModel<String> listModel;
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
     * Returns the string of the highlighted
     *
     * @return
     */
    public String getListSelectedValue() {
        return list.getSelectedValue();
    }

    public void appendToList(String elem) {
        listModel.addElement(elem);
    }

    public String getStartOffsetFieldValue() {
        return startOffsetField.getText();
    }

    public String getEndOffsetFieldValue() {
        return endOffsetField.getText();
    }

    public String getRouteFilenameFieldValue() {
        return routeFilenameField.getText();
    }

    public void clearFields() {
        startOffsetField.setText("0");
        endOffsetField.setText("0");
        routeFilenameField.setText("");
    }

    public void appendToDisplay(String text) {
        display.append(text);
    }

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
     * Bind an ActionListener to the "View" button
     *
     * @param listener
     *              the listener to be bound
     */
    public void addViewListener(ActionListener listener) {
        viewButton.addActionListener(listener);
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

    private void addButtons(Container container) {
        JPanel panel = new JPanel(new GridLayout(1, 3));

        // Grandfathered variable names, apologies
        loadButton = new JButton("New");
        viewButton = new JButton("View");
        setButton = new JButton("Update");

        panel.add(loadButton);
        panel.add(viewButton);
        panel.add(setButton);

        container.add(panel);
    }


    /**
     * Given a container, make the route filename field, start offset field,
     * end offset field and their respective labels and add them to the container
     *
     * @param container
     *              the container in which to put the buttons and their labels
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

    private void addList(Container container) {
        JPanel panel = new JPanel();

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        panel.add(list);
        container.add(panel);
    }

    private void addDisplay(Container container) {
        JPanel panel = new JPanel();

        display = new JTextArea(15, 50);
        display.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(display);
//        scrollPane.setHorizontalScrollBarPolicy(
//                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollPane);
        container.add(panel);

    }
}
