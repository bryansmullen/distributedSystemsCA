package com.bryanmullen.services.report.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ReportServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        // constants
        final int PORT = 5053;

        // create the server
        Server server = ServerBuilder
                .forPort(PORT)
                .addService(new ReportServiceImpl())
                .build();

        // start the server
        server.start();
        System.out.println("Report Server listening on port " + PORT);

        // shutdown hook to stop the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down report server...");
            server.shutdown();
            System.out.println("Server shut down");
        }));

        // block until the server is terminated
        server.awaitTermination();
    }
}
