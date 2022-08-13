package com.bryanmullen.services.client.gui;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.reportService.CowReportRequest;
import com.bryanmullen.reportService.ReportServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ReportCowReportPanel extends PanelBase {
    Logger logger = LoggerFactory.getLogger(ReportCowReportPanel.class); //
    // Logger for this class so we can log messages to the console.

    JPanel panel;
    JLabel label1;
    JLabel label2;
    JTextField textNumber1;
    JTextField textNumber2;
    JButton sendRequestButton;
    JTextArea textResponse;

    public ReportCowReportPanel() throws IOException {
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
        sendRequestButton.addActionListener(event -> doCowReport());
        panel.add(sendRequestButton);


        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        //textResponse.setSize(new Dimension(15, 30));
        panel.add(scrollPane);
    }

    private void doCowReport() {
        logger.info("Starting to do Cow Report method...");

        var stub = ReportServiceGrpc.newBlockingStub(getChannel());
        var request = CowReportRequest.newBuilder()
                .setCheckedBy(textNumber1.getText())
                .setCowId(Integer.parseInt(textNumber2.getText()))
                .build();
        var response = stub
                .withInterceptors(new ClientInterceptor())
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second
                // deadline - if the server
                // doesn't respond within 5 seconds, the call will fail
                .cowReport(request);
        textResponse.setText(String.valueOf(response));
    }
    public JPanel getPanel() {
        return panel;
    }}
