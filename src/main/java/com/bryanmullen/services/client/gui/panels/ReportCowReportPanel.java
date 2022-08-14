package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.reportService.CowReportRequest;
import com.bryanmullen.reportService.ReportServiceGrpc;
import com.bryanmullen.services.shared.ClientBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * ReportCowReportPanel - This class is used to create a panel that will be used to display the cow report panel.
 */
public class ReportCowReportPanel extends ClientBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(ReportCowReportPanel.class);

    // instance variables
    JPanel panel;
    JLabel label1;
    JLabel label2;
    JTextField textNumber1;
    JTextField textNumber2;
    JButton sendRequestButton;
    JTextArea textResponse;

    // constructor
    public ReportCowReportPanel() throws IOException {
        // call the superclass constructor with the filepath of the properties file.
        super("src/main/resources/report.properties");

        // Get the service instance using jmdns.
        getService();

        // Create the panel.
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Create the first label.
        label1 = new JLabel("Checked By");
        panel.add(label1);

        // Create the first text field.
        textNumber1 = new JTextField();
        panel.add(textNumber1);
        textNumber1.setColumns(10);

        // Create the second label.
        label2 = new JLabel("Cow ID");
        panel.add(label2);

        // Create the second text field.
        textNumber2 = new JTextField();
        panel.add(textNumber2);
        textNumber2.setColumns(10);

        // Create the send request button.
        sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(event -> doCowReport());
        panel.add(sendRequestButton);

        // Create the text area to display the response.
        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        panel.add(scrollPane);
    }

    /**
     * doCowReport - This method is used to send the cow report request to the server.
     */
    private void doCowReport() {
        // Log the start of the method.
        logger.info("Starting to do Cow Report method...");

        // input validation - make sure the first text field is not empty.
        if (textNumber1.getText().isEmpty()) {
            textResponse.setText("ERROR: Please enter a name in the checkedBy field \n");
            return;
        }

        // input validation - make sure the second text field is not empty.
        if (!textNumber2.getText().matches("-?\\d+(\\.\\d+)?")) {
            textResponse.setText("ERROR: Please enter a number in the cowID field \n");
            return;
        }

        // Create the request stub
        var stub = ReportServiceGrpc.newBlockingStub(getChannel());

        // Create the request object.
        var request = CowReportRequest.newBuilder()
                .setCheckedBy(textNumber1.getText())
                .setCowId(Integer.parseInt(textNumber2.getText()))
                .build();

        // Send the request to the server.
        var response = stub
                .withInterceptors(new ClientInterceptor()) // add the interceptor to the stub to log the hostname and
                // timestamp.
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second
                // deadline - if the server
                // doesn't respond within 5 seconds, the call will fail
                .cowReport(request);

        // set the text response to the response from the server.
        textResponse.setText(String.valueOf(response));
    }

    /**
     * getPanel - This method is used to return the panel that was created. This is used to add the panel to the gui.
     * @return - the panel.
     */
    public JPanel getPanel() {
        return panel;
    }

}
