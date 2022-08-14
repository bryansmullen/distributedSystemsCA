package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.milkingService.MilkProductionRequest;
import com.bryanmullen.milkingService.MilkingServiceGrpc;
import com.bryanmullen.services.shared.ClientBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * MilkProductionPanel - This class is used to create the panel that will be used to display the milk production panel.
 */
public class MilkProductionPanel extends ClientBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(MilkProductionPanel.class);

    // instance variables
    JPanel panel;
    JLabel label1;
    JTextField textNumber1;
    JButton sendRequestButton;
    JTextArea textResponse;

    // constructor
    public MilkProductionPanel() throws IOException {
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
        sendRequestButton.addActionListener(event -> doMilkProduction());
        panel.add(sendRequestButton);

        // create the response text area
        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        panel.add(scrollPane);
    }

    // doMilkProduction - This method is used to send a milk production request to the server.
    private void doMilkProduction() {
        // log the start of the method
        logger.info("Starting to do Milk Production method...");

        // input validation - make sure the text field is not empty.
        if (textNumber1.getText().isEmpty()) {
            textResponse.setText("ERROR: Please enter a name in the checkedBy field \n");
            return;
        }

        // create the request stub
        var stub = MilkingServiceGrpc.newBlockingStub(getChannel());

        // create the request
        var request = MilkProductionRequest.newBuilder()
                .setCheckedBy(textNumber1.getText())
                .build();

        // send the request
        var response = stub
                .withInterceptors(new ClientInterceptor()) // add the interceptor to the stub to log the hostname and
                // timestamp
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second
                // deadline - if the server
                // doesn't respond within 5 seconds, the call will fail
                .milkProduction(request);

        // set the response text area to the response
        textResponse.setText(String.valueOf(response));
    }

    // getPanel - This method is used to return the panel that was created in the constructor.  This is used to add
    // the panel to the frame in the gui class.
    public JPanel getPanel() {
        return panel;
    }
}
