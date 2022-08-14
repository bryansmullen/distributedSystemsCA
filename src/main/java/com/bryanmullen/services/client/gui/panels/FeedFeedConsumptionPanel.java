package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.feedService.FeedConsumptionRequest;
import com.bryanmullen.feedService.FeedServiceGrpc;
import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.services.shared.ClientBase;
import com.google.protobuf.Timestamp;
import org.jdesktop.swingx.JXDatePicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * FeedConsumptionPanel - This class is used to create the panel that will be used to display the feed consumption panel.
 */
public class FeedFeedConsumptionPanel extends ClientBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(FeedFeedConsumptionPanel.class);

    // instance variables
    JPanel panel;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JXDatePicker datePicker1;
    JXDatePicker datePicker2;
    JTextField textNumber3;
    JButton sendRequestButton;
    JTextArea textResponse;

    // constructor
    public FeedFeedConsumptionPanel() throws IOException {
        // call the superclass constructor with the filepath of the properties file.
        super("src/main/resources/feed.properties");

        // get the service using jmdns
        getService();

        // create the panel
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // create the first label
        label1 = new JLabel("Start Date");
        panel.add(label1);

        // create the first date picker
        datePicker1 = new JXDatePicker();
        panel.add(datePicker1);

        // create the second label
        label2 = new JLabel("End Date");
        panel.add(label2);

        // create the second date picker
        datePicker2 = new JXDatePicker();
        panel.add(datePicker2);

        // create the third label
        label3 = new JLabel("Checked By");
        panel.add(label3);

        // create the third text field (named third because the text field is the third field in the panel)
        textNumber3 = new JTextField();
        panel.add(textNumber3);
        textNumber3.setColumns(10);

        // create the send request button
        sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(event -> doFeedConsumption());
        panel.add(sendRequestButton);

        // create the response text area
        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        panel.add(scrollPane);
    }

    /**
     * doFeedConsumption - This method is used to send the feed consumption request to the server.
     */
    private void doFeedConsumption() {
        // log the start of the call
        logger.info("Starting to do Milk Production method...");

        // input validation - make sure the date fields are not null.
        if (datePicker1.getDate() == null || datePicker2.getDate() == null) {
            textResponse.setText("ERROR: Please enter a date for start date and end date \n");
            return;
        }

        // input validation - make sure the end date is after the start date.
        if (datePicker2.getDate().compareTo(datePicker1.getDate()) < 0) {
            textResponse.setText("ERROR: endDate must be after startDate");
            return;
        }

        // input validation - make sure the checked by field is not empty.
        if (textNumber3.getText().isEmpty()) {
            textResponse.setText("ERROR: Please enter a name in the checkedBy field \n");
            return;
        }

        // get a new blocking stub
        var stub = FeedServiceGrpc.newBlockingStub(getChannel());

        // create the feed consumption request
        var request = FeedConsumptionRequest.newBuilder()
                .setStartDate(Timestamp.newBuilder().setSeconds(datePicker1.getDate().getSeconds()).build())
                .setEndDate(Timestamp.newBuilder().setSeconds(datePicker2.getDate().getSeconds()).build())
                .setCheckedBy(textNumber3.getText())
                .build();

        // send the request
        var response = stub
                .withInterceptors(new ClientInterceptor()) // add the interceptor to the stub to log the hostname and
                // timestamp
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second
                // deadline - if the server
                // doesn't respond within 5 seconds, the call will fail
                .feedConsumption(request); // send the request
        textResponse.setText(String.valueOf(response));
    }

    /**
     * getPanel - This method is used to return the panel that was created. This is used to add the panel to the GUI.
     * @return - the panel that was created.
     */
    public JPanel getPanel() {
        return panel;
    }
}
