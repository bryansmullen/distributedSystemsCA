package com.bryanmullen.report.server;

import com.bryanmullen.reportService.ReportServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ReportServer extends ReportServiceGrpc.ReportServiceImplBase {
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
