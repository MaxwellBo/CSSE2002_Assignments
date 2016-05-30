package railway.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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

    // REMOVE THIS LINE AND DECLARE ANY ADDITIONAL VARIABLES YOU REQUIRE HERE

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

        String[] testData = { "0", "1" };

        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("0");
        listModel.addElement("1");

        list = new JList<>(listModel);

        // I might be able to do cool stuff here
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        p.add(list);
        c.add(p, "West");
    }

    public int getListSelectedIndex() {
        return list.getSelectedIndex();
    }

    public void addListElement(String elem) {

    }


    // TODO: Remove
    public void debugMethod() {
//        addListElement("MEME");
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
}
