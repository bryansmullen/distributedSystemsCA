package com.bryanmullen.services.client.gui;

import com.bryanmullen.services.client.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ReportGuiClient extends JPanel {


    public ReportGuiClient() throws IOException {


        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon reportIcon = createImageIcon();

        JComponent panel1 = new ReportCowReportPanel().getPanel();
        panel1.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Cow Report", reportIcon, panel1,
                "Cow Report");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = new ReportHerdReportPanel().getPanel();
        panel2.setPreferredSize(new java.awt.Dimension(1000, 300));
        tabbedPane.addTab("Herd Report", reportIcon, panel2,
                "Herd Report");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }


    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon() {
        java.net.URL imgURL = ReportGuiClient.class.getResource("report.png");
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + "report.png");
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
        frame.add(new ReportGuiClient(), BorderLayout.CENTER);

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
