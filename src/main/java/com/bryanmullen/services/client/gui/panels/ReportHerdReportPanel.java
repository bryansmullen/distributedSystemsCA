package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.reportService.HerdReportRequest;
import com.bryanmullen.reportService.HerdReportResponse;
import com.bryanmullen.reportService.ReportServiceGrpc;
import com.bryanmullen.services.shared.ClientBase;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ReportHerdReportPanel is a panel that will be used to display the herd report.
 */
public class ReportHerdReportPanel extends ClientBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(ReportHerdReportPanel.class);
    //  Instance variables
    JPanel panel;
    JLabel label1;
    JLabel label2;
    JTextField textNumber1;
    JTextField textNumber2;
    JButton sendRequestButton;
    JTextArea textResponse;

    // constructor
    public ReportHerdReportPanel() throws IOException {
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
        sendRequestButton.addActionListener(event -> {
            try {
                doHerdReport();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        panel.add(sendRequestButton);


        // Create the text area to display the response.
        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        panel.add(scrollPane);
    }

    /**
     * doHerdReport will send a herd report request to the server.
     */
    private void doHerdReport() throws InterruptedException {
        // log the start of the call
        logger.info("Starting to do Herd Report method...");

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
        var stub = ReportServiceGrpc.newStub(getChannel());

        // initialize the latch
        CountDownLatch latch = new CountDownLatch(1);

        // asynchronous call to the server
        StreamObserver<HerdReportRequest> streamObserver =
                stub
                        .withInterceptors(new ClientInterceptor()) // add the interceptor to log the hostname and timestamp
                        .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                        .herdReport(new StreamObserver<>() {
                            // onNext is called when the server sends a response
                            @Override
                            public void onNext(HerdReportResponse response) {
                                // log the response and update the text area.
                                logger.info("Herd Report Response: " + response);
                                textResponse.setText(String.valueOf(response));
                            }

                            // onError is called when the server sends an error
                            @Override
                            public void onError(Throwable t) {
                                // error handling: log the error and update the text area.
                                System.out.println("Error: " + t.getMessage());
                                latch.countDown();
                            }

                            // onCompleted is called when the server sends a completion message
                            @Override
                            public void onCompleted() {
                                // decrement the latch
                                latch.countDown();
                            }
                        });

        // create multiple requests and send them to the server
        for (int i = 0; i < 10; i++) {
            streamObserver.onNext(HerdReportRequest
                    .newBuilder()
                    .setCowId(Integer.parseInt(textNumber2.getText()) + i)
                    .setCheckedBy(textNumber1.getText())
                    .build());
        }

        // send the oncomplete message to the server to indicate that all requests have been sent.
        streamObserver.onCompleted();

        //noinspection ResultOfMethodCallIgnored
        latch.await(5, TimeUnit.SECONDS);

    }

    /**
     * getPanel will return the panel.
     *
     * @return the panel.
     */
    public JPanel getPanel() {
        return panel;
    }
}
