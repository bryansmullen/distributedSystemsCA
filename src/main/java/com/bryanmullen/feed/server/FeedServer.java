package com.bryanmullen.feed.server;

import com.bryanmullen.feedService.FeedServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class FeedServer extends FeedServiceGrpc.FeedServiceImplBase {
    public static void main(String[] args) throws IOException, InterruptedException {
        // constants
        final int PORT = 5052;

        // create the server
        Server server = ServerBuilder
                .forPort(PORT)
                .build();

        // start the server
        server.start();
        System.out.println("Server listening on port " + PORT);

        // shutdown hook to stop the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.shutdown();
            System.out.println("Server shut down");
        }));

        // block until the server is terminated
        server.awaitTermination();
    }
}
