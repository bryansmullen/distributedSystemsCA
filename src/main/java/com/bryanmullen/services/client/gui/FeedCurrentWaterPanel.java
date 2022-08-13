package com.bryanmullen.services.client.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FeedCurrentWaterPanel extends PanelBase {
    JPanel panel;
    JLabel label1;
    JTextField textNumber1;
    JButton sendRequestButton;
    JTextArea textResponse;

    public FeedCurrentWaterPanel() throws IOException {
        super("src/main/resources/feed.properties");
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        label1 = new JLabel("Checked By");
        panel.add(label1);

        textNumber1 = new JTextField();
        panel.add(textNumber1);
        textNumber1.setColumns(10);

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
