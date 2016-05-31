package railway.gui;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
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
    private JFormattedTextField startOffsetField;
    private JFormattedTextField endOffsetField;
    private JTextField routeFilenameField;
    private JTextArea display;

    /**
     * Creates a new Railway Manager window.
     */
    public RailwayView(RailwayModel model) {
        this.model = model;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Train Management System");
        setBounds(400, 200, 250, 300);
        Container c = getContentPane();

        addButtons(c);
        addList(c);
        addFields(c);
        addDisplay(c);
    }

    private void addButtons(Container c) {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3, 3));

        loadButton = new JButton("Load");
        viewButton = new JButton("View");
        setButton = new JButton("Set");

        p.add(loadButton);
        p.add(viewButton);
        p.add(setButton);

        c.add(p, "Center");
    }

    private void addList(Container c) {
        JPanel p = new JPanel();

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);

        // I might be able to do cool stuff here
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        p.add(list);
        c.add(p, "West");
    }

    private void addDisplay(Container c) {
        JPanel p = new JPanel();

        display = new JTextArea(10, 40);
        display.setEditable(false);

        p.add(display);
        c.add(p, "South");

    }

    public String getListSelectedValue() {
        return list.getSelectedValue();
    }

    // TODO: Rename this method
    public void addListElement(String elem) {
        listModel.addElement(elem);
    }

    public void appendToDisplay(String text) {
        display.append(text);
    }

    public void clearDisplay() {
        display.setText("");
    }

    public void clearFields() {
        startOffsetField.setText("");
        endOffsetField.setText("");
        routeFilenameField.setText("");
    }

    private void addFields(Container c) {
        JPanel p = new JPanel();
        MaskFormatter formatter;


        try {
            formatter = new MaskFormatter("#####");

            routeFilenameField = new JTextField(15);
            startOffsetField = new JFormattedTextField(formatter);
            startOffsetField.setColumns(10);
            endOffsetField = new JFormattedTextField(formatter);
            endOffsetField.setColumns(10);
            p.add(routeFilenameField);
            p.add(startOffsetField);
            p.add(endOffsetField);
            c.add(p, "East");
        }
        catch (Exception e) {
            makeDialogBox("You realy screwed up", "You meme");
        }

    }


    public void makeDialogBox(String eClass, String eMessage) {
        JOptionPane.showMessageDialog(
                this
                , eMessage
                , eClass
                , JOptionPane.ERROR_MESSAGE);
    }

    public void addLoadListener(ActionListener pl) {
        loadButton.addActionListener(pl);
    }

    public void addViewListener(ActionListener pl) {
        viewButton.addActionListener(pl);
    }

    public void addSetListener(ActionListener pl) {
        setButton.addActionListener(pl);
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
}
