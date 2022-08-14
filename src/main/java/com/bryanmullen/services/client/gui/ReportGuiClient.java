package com.bryanmullen.services.client.gui;

import com.bryanmullen.services.client.gui.panels.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * ReportGuiClient - This class is used to GUI the report service.
 */
public class ReportGuiClient extends JPanel {
    // Logger for this class so we can log messages to the console.
    static Logger logger = LoggerFactory.getLogger(FeedGuiClient.class);

    // constructor
    public ReportGuiClient() throws IOException {
        // call the superclass constructor for JPanel with GridLayout.
        super(new GridLayout(1, 1));

        // create a tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon cowReportIcon = createImageIcon("/icons/cowReport.png");
        ImageIcon herdReportIcon = createImageIcon("/icons/herdReport.png");

        // create and append first panel - ReportCowReportPanel
        JComponent panel1 = new ReportCowReportPanel().getPanel();
        panel1.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Cow Report", cowReportIcon, panel1,
                "Cow Report");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        // create and append second panel - ReportHerdReportPanel
        JComponent panel2 = new ReportHerdReportPanel().getPanel();
        panel2.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Herd Report", herdReportIcon, panel2,
                "Herd Report");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        // Add the tabbed pane to this panel.
        add(tabbedPane);

        // The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }


    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String filePath) {
        // get the image from the resources' folder.
        java.net.URL imgURL = ReportGuiClient.class.getResource(filePath);

        // if the image is not found, return null.
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            logger.error("Couldn't find " + filePath);
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
        frame.add(new ReportGuiClient(), BorderLayout.CENTER);

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
