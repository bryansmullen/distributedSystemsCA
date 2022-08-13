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
    Logger logger = LoggerFactory.getLogger(FeedAddToFeedPanel.class); //
    // Logger for this class so we can log messages to the console.
    JPanel panel;
    JLabel label1;
    JLabel label2;
    JTextField textNumber1;
    JTextField textNumber2;
    JButton sendRequestButton;
    JTextArea textResponse;

    public FeedAddToFeedPanel() throws IOException {
        super("src/main/resources/feed.properties");
        getService();

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        label1 = new JLabel("Feed Mass To Add");
        panel.add(label1);

        textNumber1 = new JTextField();
        panel.add(textNumber1);
        textNumber1.setColumns(10);

        label2 = new JLabel("Added By");
        panel.add(label2);

        textNumber2 = new JTextField();
        panel.add(textNumber2);
        textNumber2.setColumns(10);

        sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(event -> {
            try {
                doAddToFeed();
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

    public void doAddToFeed() throws InterruptedException {
        // log the start of the call
        logger.info("Starting to do Add To Feed Available method...");

        if (!textNumber1.getText().matches("-?\\d+(\\.\\d+)?")) {
            textResponse.setText("ERROR: Please enter a number in the feedMassToAdd field \n");
            return;
        }

        if (textNumber2.getText().isEmpty()) {
            textResponse.setText("ERROR: Please enter a name in the addedBy field \n");
            return;
        }

        var stub = FeedServiceGrpc.newStub(getChannel());

        CountDownLatch latch = new CountDownLatch(1);

        var streamObserver = stub.withInterceptors(new ClientInterceptor()).withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                .addToFeedAvailable(new StreamObserver<>() {
                    @Override
                    public void onNext(AddToFeedResponse response) {
                        logger.info("Received response: " + response);
                        textResponse.append("Received response: " + response + "\n");
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {
                        latch.countDown();
                    }
                });

        streamObserver.onNext(
                AddToFeedRequest.newBuilder()
                        .setFeedMassToAdd(Double.parseDouble(textNumber1.getText()))
                        .setAddedBy(textNumber2.getText())
                        .build());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        streamObserver.onCompleted();
        //noinspection ResultOfMethodCallIgnored
        latch.await(5, TimeUnit.SECONDS);
    }


    public JPanel getPanel() {
        return panel;
    }
}
