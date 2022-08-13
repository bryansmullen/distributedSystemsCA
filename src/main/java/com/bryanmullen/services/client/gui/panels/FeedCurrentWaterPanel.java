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
    Logger logger = LoggerFactory.getLogger(FeedCurrentWaterPanel.class); //
    // Logger for this class so we can log messages to the console.
    JPanel panel;
    JLabel label1;
    JTextField textNumber1;
    JButton sendRequestButton;
    JTextArea textResponse;

    public FeedCurrentWaterPanel() throws IOException {
        super("src/main/resources/feed.properties");
        getService();

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        label1 = new JLabel("Checked By");
        panel.add(label1);

        textNumber1 = new JTextField();
        panel.add(textNumber1);
        textNumber1.setColumns(10);

        sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(event -> doCurrentWater());
        panel.add(sendRequestButton);


        textResponse = new JTextArea(10, 20);
        textResponse.setLineWrap(true);
        textResponse.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textResponse);
        //textResponse.setSize(new Dimension(15, 30));
        panel.add(scrollPane);
    }

    private void doCurrentWater() {
        // log the start of the call
        logger.info("Starting to do Current Water Available method...");


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
        stub.withInterceptors(new ClientInterceptor())
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                .currentWaterAvailable(request, new StreamObserver<>() {
                    @Override
                    public void onNext(CurrentWaterResponse response) {
                        logger.info("onNext: " + response);
                        textResponse.append(String.valueOf(response));
                    }

                    @Override
                    public void onError(Throwable t) {
                        logger.info("onError: " + t);
                        textResponse.setText(t.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        logger.info("onCompleted");
                    }
                });
    }

    public JPanel getPanel() {
        return panel;
    }
}
