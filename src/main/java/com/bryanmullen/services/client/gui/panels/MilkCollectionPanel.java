package com.bryanmullen.services.client.gui.panels;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.milkingService.MilkCollectionRequest;
import com.bryanmullen.milkingService.MilkCollectionResponse;
import com.bryanmullen.milkingService.MilkingServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MilkCollectionPanel extends PanelBase {
    Logger logger = LoggerFactory.getLogger(MilkCollectionPanel.class); //
    // Logger for this class so we can log messages to the console.
    JPanel panel;
    JLabel label1;
    JTextField textNumber1;
    JButton sendRequestButton;
    JTextArea textResponse;

    public MilkCollectionPanel() throws IOException {
        super("src/main/resources/milking.properties");
        getService();

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        label1 = new JLabel("Checked By");
        panel.add(label1);

        textNumber1 = new JTextField();
        panel.add(textNumber1);
        textNumber1.setColumns(10);

        sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(event -> doMilkCollection());
        panel.add(sendRequestButton);

        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        //textResponse.setSize(new Dimension(15, 30));
        panel.add(scrollPane);
    }

    private void doMilkCollection() {
        logger.info("Starting to do Milk Collection method...");

        if (textNumber1.getText().isEmpty()) {
            textResponse.setText("ERROR: Please enter a name in the checkedBy field \n");
            return;
        }

        var stub = MilkingServiceGrpc.newStub(getChannel());
        var request = MilkCollectionRequest.newBuilder()
                .setCheckedBy(textNumber1.getText())
                .build();
        stub.withInterceptors(new ClientInterceptor())
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second
                // deadline - if the server
                // doesn't respond within 5 seconds, the call will fail
                .milkCollection(request, new StreamObserver<>() {
                    @Override
                    public void onNext(MilkCollectionResponse response) {
                        logger.info("onNext: " + response);
                        textResponse.append(response + "\n");
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }
    public JPanel getPanel() {
        return panel;
    }
}
