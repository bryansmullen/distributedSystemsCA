package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.feedService.AddToFeedRequest;
import com.bryanmullen.feedService.AddToFeedResponse;
import com.bryanmullen.feedService.FeedServiceGrpc;
import com.bryanmullen.interceptors.ClientInterceptor;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FeedAddToFeedPanel extends PanelBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(FeedAddToFeedPanel.class);

    // Instance variables
    JPanel panel;
    JLabel label1;
    JLabel label2;
    JTextField textNumber1;
    JTextField textNumber2;
    JButton sendRequestButton;
    JTextArea textResponse;

    /**
     *
     */
    public FeedAddToFeedPanel() throws IOException {

        // call the superclass constructor with the filepath of the properties file.
        super("src/main/resources/feed.properties");

        // Get the service instance using jmdns.
        getService();

        // Create the panel.
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Create the first label.
        label1 = new JLabel("Feed Mass To Add");
        panel.add(label1);

        // Create the first text field.
        textNumber1 = new JTextField();
        panel.add(textNumber1);
        textNumber1.setColumns(10);

        // Create the second label.
        label2 = new JLabel("Added By");
        panel.add(label2);

        // Create the second text field.
        textNumber2 = new JTextField();
        panel.add(textNumber2);
        textNumber2.setColumns(10);

        // Create the send request button.
        sendRequestButton = new JButton("Send Request");

        // Add the action listener to the send request button so that the method is called when the button is pressed.
        sendRequestButton.addActionListener(event -> {
            try {
                doAddToFeed();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Add the send request button to the panel.
        panel.add(sendRequestButton);

        // Create the text area to display the response.
        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        panel.add(scrollPane);
    }

    /**
     * doAddToFeed is a method that sends an add to feed request to the server.
     */
    public void doAddToFeed() throws InterruptedException {
        // log the start of the call
        logger.info("Starting to do Add To Feed Available method...");

        // Input validation for text field 1.
        if (!textNumber1.getText().matches("-?\\d+(\\.\\d+)?")) {
            textResponse.setText("ERROR: Please enter a number in the feedMassToAdd field \n");
            return;
        }

        // Input validation for text field 2.
        if (textNumber2.getText().isEmpty()) {
            textResponse.setText("ERROR: Please enter a name in the addedBy field \n");
            return;
        }

        // Create the request stub.
        var stub = FeedServiceGrpc.newStub(getChannel());

        // initialize the latch.
        CountDownLatch latch = new CountDownLatch(1);

        // asynchronous call to the server using the stub.
        var streamObserver = stub
                .withInterceptors(new ClientInterceptor()) // add the interceptor to the stub to log the hostname and
                // timestamp.
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                // by then, the call will fail.
                .addToFeedAvailable(new StreamObserver<>() { // create a stream observer to handle the response.
                    // onNext is called when the server sends a response.
                    @Override
                    public void onNext(AddToFeedResponse response) {
                        // log the response and update the text area.
                        logger.info("Received response: " + response);
                        textResponse.append("Received response: " + response + "\n");
                    }

                    // onError is called if the server returns an error.
                    @Override
                    public void onError(Throwable t) {
                        // error handling - log the error and set the text area to display the error.
                        logger.info("Error: " + t.getMessage());
                        textResponse.append("Error: " + t.getMessage() + "\n");
                    }

                    // onCompleted is called when the server returns a response.
                    @Override
                    public void onCompleted() {
                        // decrement the latch so the program can continue.
                        latch.countDown();
                    }
                });

        // create the request object.
        streamObserver.onNext(
                AddToFeedRequest.newBuilder()
                        .setFeedMassToAdd(Double.parseDouble(textNumber1.getText()))
                        .setAddedBy(textNumber2.getText())
                        .build());

        // wait for 500 milliseconds between each request.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // send the request.
        streamObserver.onCompleted();

        //noinspection ResultOfMethodCallIgnored
        latch.await(5, TimeUnit.SECONDS);
    }

    /**
     * getPanel returns the panel. This is used by the GUI to display the panel.
     */
    public JPanel getPanel() {
        return panel;
    }
}
