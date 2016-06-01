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
        setTitle("Train Management System");
        setBounds(400, 200, 250, 300);
        Container container = getContentPane();

        addButtons(container);
        addList(container);
        addFields(container);
        addDisplay(container);
    }

    public void makeDialogBox(String eClass, String eMessage) {
        JOptionPane.showMessageDialog(
                this
                , eMessage
                , eClass
                , JOptionPane.ERROR_MESSAGE);
    }

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

    public void addLoadListener(ActionListener listener) {
        loadButton.addActionListener(listener);
    }

    public void addViewListener(ActionListener listener) {
        viewButton.addActionListener(listener);
    }

    public void addSetListener(ActionListener listener) {
        setButton.addActionListener(listener);
    }

    private void addButtons(Container container) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        loadButton = new JButton("Load");
        viewButton = new JButton("View");
        setButton = new JButton("Set");

        panel.add(loadButton);
        panel.add(viewButton);
        panel.add(setButton);

        container.add(panel, "Center");
    }

    private void addFields(Container container) {
        JPanel panel = new JPanel();

        NumberFormat onlyNumbers = NumberFormat.getNumberInstance();
        onlyNumbers.setGroupingUsed(false);

        routeFilenameField = new JTextField(15);

        startOffsetField = new JFormattedTextField(onlyNumbers);
        startOffsetField.setValue(new Double(0));
        startOffsetField.setColumns(7);

        endOffsetField = new JFormattedTextField(onlyNumbers);
        endOffsetField.setValue(new Double(0));
        endOffsetField.setColumns(7);

        panel.add(routeFilenameField);
        panel.add(startOffsetField);
        panel.add(endOffsetField);
        container.add(panel, "East");

    }

    private void addList(Container container) {
        JPanel panel = new JPanel();

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        panel.add(list);
        container.add(panel, "West");
    }

    private void addDisplay(Container container) {
        JPanel panel = new JPanel();

        display = new JTextArea(10, 40);
        display.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(display);
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(display);
        panel.add(scrollPane);
        container.add(panel, "South");

    }
}
