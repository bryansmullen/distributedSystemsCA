package com.bryanmullen.services.client.gui;

import com.bryanmullen.services.client.gui.panels.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * MilkGuiClient - This class is used to GUI the milk service.
 */
public class MilkGuiClient extends JPanel {
    // Logger for this class so we can log messages to the console.
    static Logger logger = LoggerFactory.getLogger(MilkGuiClient.class);

    // constructor
    public MilkGuiClient() throws IOException {
        // call the superclass constructor for JPanel with GridLayout.
        super(new GridLayout(1, 1));

        // create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon milkIcon = createImageIcon();

        // create and append first panel - MilkCollectionPanel
        JComponent panel1 = new MilkCollectionPanel().getPanel();
        panel1.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Milk Collection", milkIcon, panel1,
                "Milk Collection");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        // create and append second panel - MilkProductionPanel
        JComponent panel2 = new MilkProductionPanel().getPanel();
        panel2.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Milk Production", milkIcon, panel2,
                "Milking Production");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        // create and append third panel - MilkCurrentCowPanel
        JComponent panel3 = new MilkCurrentCowPanel().getPanel();
        panel3.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Milk Current Cow", milkIcon, panel3,
                "Milk Collection");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        // Add the tabbed pane to this panel.
        add(tabbedPane);

        // The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }


    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon() {
        // get the image from the resources' folder.
        java.net.URL imgURL = MilkGuiClient.class.getResource("/milk.png");

        // if the image is not found, return null.
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            logger.error("Couldn't find milk.png");
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety, this method should be invoked from the event dispatch thread.
     */
    private static void createAndShowGUI() throws IOException {
        // Create the frame.
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the FeedGuiClient to the frame.
        frame.add(new MilkGuiClient(), BorderLayout.CENTER);

        // Pack the frame and make it visible.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(() -> {
            // Turn off metal's use of bold fonts
            UIManager.put("swing.boldMetal", Boolean.FALSE);

            // try to create and show the GUI.
            try {
                createAndShowGUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
