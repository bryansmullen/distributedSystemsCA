package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.feedService.CurrentWaterRequest;
import com.bryanmullen.feedService.CurrentWaterResponse;
import com.bryanmullen.feedService.FeedServiceGrpc;
import com.bryanmullen.interceptors.ClientInterceptor;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FeedCurrentWaterPanel extends PanelBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(FeedCurrentWaterPanel.class);

    // instance variables
    JPanel panel;
    JLabel label1;
    JTextField textNumber1;
    JButton sendRequestButton;
    JTextArea textResponse;

    // constructor
    public FeedCurrentWaterPanel() throws IOException {
        // call the superclass constructor with the filepath of the properties file.
        super("src/main/resources/feed.properties");

        // get the service using jmdns
        getService();

        // create the panel
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // create the first label
        label1 = new JLabel("Checked By");
        panel.add(label1);

        // create the first text field
        textNumber1 = new JTextField();
        panel.add(textNumber1);
        textNumber1.setColumns(10);

        // create the send request button
        sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(event -> doCurrentWater());
        panel.add(sendRequestButton);

        // create the response text area
        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        panel.add(scrollPane);
    }

    /**
     * doCurrentWater - sends a current water request to the server.
     */
    private void doCurrentWater() {
        // log the start of the call
        logger.info("Starting to do Current Water Available method...");

        // input validation - make sure the text field is not empty.
        if (textNumber1.getText().isEmpty()) {
            textResponse.setText("ERROR: Please enter a name in the checkedBy field \n");
            return;
        }

        // create the client stub for the service
        var stub = FeedServiceGrpc.newStub(getChannel());

        // create the request object
        var request = CurrentWaterRequest
                .newBuilder()
                .setCheckedBy("Bryan")
                .build();

        // get the response from the server by calling the service with a new request
        stub
                .withInterceptors(new ClientInterceptor()) // add the interceptor to the stub to log the hostname and
                // timestamp
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                // in time, the call will fail.
                .currentWaterAvailable(request, new StreamObserver<>() {
                    // onNext - called when the server sends a response
                    @Override
                    public void onNext(CurrentWaterResponse response) {
                        // log the response and update the text area
                        logger.info("onNext: " + response);
                        textResponse.append(String.valueOf(response));
                    }

                    // error handling - called when the server sends an error
                    @Override
                    public void onError(Throwable t) {
                        // log the error and update the text area
                        logger.info("onError: " + t);
                        textResponse.setText(t.getMessage());
                    }

                    // onCompleted - called when the server sends a complete message
                    @Override
                    public void onCompleted() {
                        logger.info("onCompleted");
                    }
                });
    }

    /**
     * getPanel - returns the panel - this is used by the GUI to add the panel to the frame.
     * @return the panel.
     */
    public JPanel getPanel() {
        return panel;
    }
}
