package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.milkingService.MilkCollectionRequest;
import com.bryanmullen.milkingService.MilkCollectionResponse;
import com.bryanmullen.milkingService.MilkingServiceGrpc;
import com.bryanmullen.services.shared.ClientBase;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * MilkCollectionPanel - This class is used to create the panel that will be used to display the milk collection panel.
 */
public class MilkCollectionPanel extends ClientBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(MilkCollectionPanel.class);
    // instance variables
    JPanel panel;
    JLabel label1;
    JTextField textNumber1;
    JButton sendRequestButton;
    JTextArea textResponse;

    public MilkCollectionPanel() throws IOException {
        // call the superclass constructor with the filepath of the properties file.
        super("src/main/resources/milking.properties");

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
        sendRequestButton.addActionListener(event -> doMilkCollection());
        panel.add(sendRequestButton);

        // create the response text area
        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        panel.add(scrollPane);
    }

    /**
     * doMilkCollection - This method is used to send a request to the server to get the milk collection.
     */
    private void doMilkCollection() {
        // log the start of the call
        logger.info("Starting to do Milk Collection method...");

        // input validation - make sure the text field is not empty.
        if (textNumber1.getText().isEmpty()) {
            textResponse.setText("ERROR: Please enter a name in the checkedBy field \n");
            return;
        }

        // create the client stub for the service
        var stub = MilkingServiceGrpc.newStub(getChannel());

        // create the request
        var request = MilkCollectionRequest.newBuilder()
                .setCheckedBy(textNumber1.getText())
                .build();

        // create the stream observer
        stub
                .withInterceptors(new ClientInterceptor()) // add the interceptor to the client stub to log the
                // hostname and timestamp
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second
                // deadline - if the server
                // doesn't respond within 5 seconds, the call will fail
                .milkCollection(request, new StreamObserver<>() {

                    // onNext - this method is called when the server sends a response
                    @Override
                    public void onNext(MilkCollectionResponse response) {
                        // log the response and append it to the text area
                        logger.info("onNext: " + response);
                        textResponse.append(response + "\n");
                    }

                    // onError - this method is called when the server sends an error
                    @Override
                    public void onError(Throwable t) {
                        // error handling: log the error and append it to the text area
                        logger.error("onError: " + t.getMessage());
                        textResponse.append("ERROR: " + t.getMessage() + "\n");
                    }

                    // onCompleted - this method is called when the server sends a completion message
                    @Override
                    public void onCompleted() {
                        // log the completion message and append it to the text area
                        logger.info("onCompleted");
                        textResponse.append("\n");
                    }
                });
    }

    /**
     * getPanel - This method is used to get the panel. This is used to add the panel to the frame in the gui.
     * @return - the panel.
     */
    public JPanel getPanel() {
        return panel;
    }
}
