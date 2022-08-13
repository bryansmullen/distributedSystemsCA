package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.reportService.HerdReportRequest;
import com.bryanmullen.reportService.HerdReportResponse;
import com.bryanmullen.reportService.ReportServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReportHerdReportPanel extends PanelBase {
    Logger logger = LoggerFactory.getLogger(ReportHerdReportPanel.class); //
    // Logger for this class so we can log messages to the console.
    JPanel panel;
    JLabel label1;
    JLabel label2;
    JTextField textNumber1;
    JTextField textNumber2;
    JButton sendRequestButton;
    JTextArea textResponse;

    public ReportHerdReportPanel() throws IOException {
        super("src/main/resources/report.properties");

        getService();

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        label1 = new JLabel("Checked By");
        panel.add(label1);

        textNumber1 = new JTextField();
        panel.add(textNumber1);
        textNumber1.setColumns(10);

        label2 = new JLabel("Cow ID");
        panel.add(label2);

        textNumber2 = new JTextField();
        panel.add(textNumber2);
        textNumber2.setColumns(10);

        sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(event -> {
            try {
                doHerdReport();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        panel.add(sendRequestButton);


        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        //textResponse.setSize(new Dimension(15, 30));
        panel.add(scrollPane);
    }

    private void doHerdReport() throws InterruptedException {
        logger.info("Starting to do Herd Report method...");


        if (textNumber1.getText().isEmpty()) {
            textResponse.setText("ERROR: Please enter a name in the checkedBy field \n");
            return;
        }

        if (!textNumber2.getText().matches("-?\\d+(\\.\\d+)?")) {
            textResponse.setText("ERROR: Please enter a number in the cowID field \n");
            return;
        }

        var stub = ReportServiceGrpc.newStub(getChannel());


        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<HerdReportRequest> streamObserver =
                stub.withInterceptors(new ClientInterceptor()).withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                        .herdReport(new StreamObserver<>() {
                            // client side - streamObserver is the client side of the stream
                            @Override
                            public void onNext(HerdReportResponse response) {
                                logger.info("Herd Report Response: " + response);
                                textResponse.setText(String.valueOf(response));
                            }

                            @Override
                            public void onError(Throwable t) {
                                System.out.println("Error: " + t.getMessage());
                                latch.countDown();
                            }

                            @Override
                            public void onCompleted() {
                                System.out.println("Completed");
                                latch.countDown();
                            }
                        });

        // server side - send the request
        for (int i = 0; i < 10; i++) {
            streamObserver.onNext(HerdReportRequest
                    .newBuilder()
                    .setCowId(Integer.parseInt(textNumber2.getText()) + i)
                    .setCheckedBy(textNumber1.getText())
                    .build());

        }

        streamObserver.onCompleted();
        //noinspection ResultOfMethodCallIgnored
        latch.await(5, TimeUnit.SECONDS);

    }
    public JPanel getPanel() {
        return panel;
    }
}
