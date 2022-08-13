package com.bryanmullen.services.client.gui;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FeedFeedConsumptionPanel extends PanelBase {
    JPanel panel;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JXDatePicker datePicker1;

    JXDatePicker datePicker2;
    JTextField textNumber3;
    JButton sendRequestButton;
    JTextArea textResponse;

    public FeedFeedConsumptionPanel() throws IOException {
        super("src/main/resources/feed.properties");
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        label1 = new JLabel("Start Date");
        panel.add(label1);

        datePicker1 = new JXDatePicker();
        panel.add(datePicker1);

        label2 = new JLabel("End Date");
        panel.add(label2);

        datePicker2 = new JXDatePicker();
        panel.add(datePicker2);


        label3 = new JLabel("Checked By");
        panel.add(label3);

        textNumber3 = new JTextField();
        panel.add(textNumber3);
        textNumber3.setColumns(10);

        sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(event -> System.out.println("Clicked"));
        panel.add(sendRequestButton);


        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        //textResponse.setSize(new Dimension(15, 30));
        panel.add(scrollPane);
    }

    public JPanel getPanel() {
        return panel;
    }
}
