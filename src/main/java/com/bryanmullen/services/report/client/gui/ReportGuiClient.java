package com.bryanmullen.services.report.client.gui;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.reportService.CowReportRequest;
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
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReportGuiClient extends ClientBase {
    Logger logger = LoggerFactory.getLogger(ReportGuiClient.class); //
    // Logger for this class so we can log messages to the console.

    private final JFrame frame;
    private final JTextField textNumber1;
    private final JTextField textNumber2;
    private final JTextArea textResponse;

    public ReportGuiClient(String propertiesFilePath) throws IOException {
        super(propertiesFilePath);
        frame = new JFrame();
        frame.setTitle("Client - Service Controller");
        frame.setBounds(100, 100, 500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JPanel panel_service_1 = new JPanel();
        frame.getContentPane().add(panel_service_1);
        panel_service_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel label1 = new JLabel("Cow ID");
        panel_service_1.add(label1);

        textNumber1 = new JTextField();
        panel_service_1.add(textNumber1);
        textNumber1.setColumns(10);

        JLabel label2 = new JLabel("Checked By");
        panel_service_1.add(label2);

        textNumber2 = new JTextField();
        panel_service_1.add(textNumber2);
        textNumber2.setColumns(10);

        JComboBox<String> chosenOperation = new JComboBox<>();
        chosenOperation.setModel(new DefaultComboBoxModel<>(new String[]{"Cow Report", "Herd Report"}));
        panel_service_1.add(chosenOperation);

        var sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(event -> {
            switch (Objects.requireNonNull(chosenOperation.getSelectedItem()).toString()) {
                case "Cow Report" -> doCowReport();
                case "Herd Report" -> {
                    try {
                        doHerdReport();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }


        });
        panel_service_1.add(sendRequestButton);
        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textResponse);

        //textResponse.setSize(new Dimension(15, 30));
        panel_service_1.add(scrollPane);


        JPanel panel_service_2 = new JPanel();
        frame.getContentPane().add(panel_service_2);

        JPanel panel_service_3 = new JPanel();
        frame.getContentPane().add(panel_service_3);
    }

    public void run() {
        frame.setVisible(true);
    }

    public void doCowReport() {
        logger.info("Starting to do Cow Report method...");
        // create the client stub for the service
        logger.info(getChannel().toString());
        var stub = ReportServiceGrpc.newBlockingStub(getChannel());

        // get input from user
        var cowId = Integer.parseInt(textNumber1.getText());
        var checkedBy = textNumber2.getText();

        // get the response from the server by calling the service with a new request
        var response =
                stub.withInterceptors(new ClientInterceptor())
                        .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                        .cowReport(CowReportRequest
                                .newBuilder()
                                .setCowId(cowId)
                                .setCheckedBy(checkedBy)
                                .build());

        // print the response to the console
        textResponse.append(String.valueOf(response));


        // close the channel
        //        super.closeChannel();
    }

    public void doHerdReport() throws InterruptedException {
        // log the start of the call
        logger.info("Starting to do Herd Report method...");

        var stub = ReportServiceGrpc.newStub(getChannel());

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<HerdReportRequest> streamObserver =
                stub.withInterceptors(new ClientInterceptor()).withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                        .herdReport(new StreamObserver<>() {
                            // client side - streamObserver is the client side of the stream
                            @Override
                            public void onNext(HerdReportResponse response) {
                                textResponse.append(String.valueOf(response));
                            }

                            @Override
                            public void onError(Throwable t) {
                                logger.error(t.getMessage());
                                latch.countDown();
                            }

                            @Override
                            public void onCompleted() {
                                textResponse.append("Completed\n");
                                latch.countDown();
                            }
                        });

        var cowId = Integer.parseInt(textNumber1.getText());
        var checkedBy = textNumber2.getText();

        // server side - send the request
        for (int i = 0; i < 10; i++) {
            streamObserver.onNext(HerdReportRequest
                    .newBuilder()
                    .setCowId(cowId)
                    .setCheckedBy(checkedBy)
                    .build());
        }

        streamObserver.onCompleted();
        //noinspection ResultOfMethodCallIgnored
        latch.await(5, TimeUnit.SECONDS);

    }
}
