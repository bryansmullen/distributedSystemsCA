package com.bryanmullen.services.client.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.*;

public class GuiClient extends JPanel {


    public GuiClient() throws IOException {


        super(new GridLayout(1, 1));


        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon milkIcon = createImageIcon("milk.png");
        ImageIcon feedIcon = createImageIcon("feed.png");
        ImageIcon reportIcon = createImageIcon("report.png");

        JComponent panel1 = new MilkCollectionPanel().getPanel();
        panel1.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Milk Collection", milkIcon, panel1,
                "Milk Collection");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = new MilkProductionPanel().getPanel();
        panel2.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Milk Production", milkIcon, panel2,
                "Milking Production");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = new MilkCurrentCowPanel().getPanel();
        panel3.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Milk Current Cow", milkIcon, panel3,
                "Milk Collection");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent panel4 = new FeedAddToFeedPanel().getPanel();
        panel4.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Add To Feed", feedIcon, panel4,
                "Add To Feed");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        JComponent panel5 = new FeedCurrentWaterPanel().getPanel();
        panel5.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Current Water Available", feedIcon, panel5,
                "Current Water Available");
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);

        JComponent panel6 = new FeedFeedConsumptionPanel().getPanel();
        panel6.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Feed Consumption", feedIcon, panel6,
                "Feed Consumption");
        tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);

        JComponent panel7 = new ReportCowReportPanel().getPanel();
        panel7.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Cow Report", reportIcon, panel7,
                "Cow Report");
        tabbedPane.setMnemonicAt(6, KeyEvent.VK_7);

        JComponent panel8 = new ReportHerdReportPanel().getPanel();
        panel8.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Herd Report", reportIcon, panel8,
                "Herd Report");
        tabbedPane.setMnemonicAt(7, KeyEvent.VK_8);


        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }


    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GuiClient.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety, this method should be invoked from the event dispatch thread.
     */
    private static void createAndShowGUI() throws IOException {
        //Create and set up the window.
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new GuiClient(), BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(() -> {
            //Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            try {
                createAndShowGUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}