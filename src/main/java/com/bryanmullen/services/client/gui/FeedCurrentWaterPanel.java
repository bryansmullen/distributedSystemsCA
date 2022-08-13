package com.bryanmullen.services.client.gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FeedCurrentWaterPanel extends PanelBase {
    JPanel panel;
    JLabel label1;
    JLabel label2;
    JTextField textNumber1;
    JTextField textNumber2;
    JButton sendRequestButton;
    JTextArea textResponse;

    public FeedCurrentWaterPanel() throws IOException {
        super("src/main/resources/milking.properties");
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        label1 = new JLabel("Cow ID");
        panel.add(label1);

        textNumber1 = new JTextField();
        panel.add(textNumber1);
        textNumber1.setColumns(10);

        label2 = new JLabel("Checked By");
        panel.add(label2);

        textNumber2 = new JTextField();
        panel.add(textNumber2);
        textNumber2.setColumns(10);

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
