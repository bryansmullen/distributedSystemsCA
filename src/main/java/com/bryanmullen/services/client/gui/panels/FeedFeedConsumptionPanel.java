package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.feedService.FeedConsumptionRequest;
import com.bryanmullen.feedService.FeedServiceGrpc;
import com.bryanmullen.interceptors.ClientInterceptor;
import com.google.protobuf.Timestamp;
import org.jdesktop.swingx.JXDatePicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FeedFeedConsumptionPanel extends PanelBase {
    Logger logger = LoggerFactory.getLogger(FeedFeedConsumptionPanel.class); //
    // Logger for this class so we can log messages to the console.
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
        getService();

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
        sendRequestButton.addActionListener(event -> doFeedConsumption());
        panel.add(sendRequestButton);


        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        //textResponse.setSize(new Dimension(15, 30));
        panel.add(scrollPane);
    }

    private void doFeedConsumption() {
        logger.info("Starting to do Milk Production method...");

        var stub = FeedServiceGrpc.newBlockingStub(getChannel());
        var request = FeedConsumptionRequest.newBuilder()
                .setStartDate(Timestamp.newBuilder().setSeconds(datePicker1.getDate().getSeconds()).build())
                .setEndDate(Timestamp.newBuilder().setSeconds(datePicker2.getDate().getSeconds()).build())
                .setCheckedBy(textNumber3.getText())
                .build();
        var response = stub
                .withInterceptors(new ClientInterceptor())
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second
                // deadline - if the server
                // doesn't respond within 5 seconds, the call will fail
                .feedConsumption(request);
        textResponse.setText(String.valueOf(response));
    }
    public JPanel getPanel() {
        return panel;
    }
}
