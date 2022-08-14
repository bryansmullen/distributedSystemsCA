package com.bryanmullen.services.report.server;

import com.bryanmullen.interceptors.ServerInterceptor;
import com.bryanmullen.services.shared.ServerBase;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;


import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * The ReportServer class is the server that will be used to serve the feed service. This server will be responsible for
 * initializing the server, mounting the services, implementing the interceptors, whether to use SSL, and finally starting the server.
 */
public class ReportServer extends ServerBase {
    // constructor
    public ReportServer(String propertiesFilePath, BindableService bindableService) throws IOException {
        super(propertiesFilePath, bindableService);
    }

    // Build the server
    Server reportServer = ServerBuilder
            .forPort(Integer.parseInt(getProperties().getProperty( // The port that the server will be listening on.
                    "service_port"))) // Get the port from the properties file
            .useTransportSecurity( // useTransportSecurity will determine whether to use SSL.
                    getFile("/my-public-key-cert.pem"), // public Key
                    getFile("/my-private-key.pem")) // private key
            .addService(new ReportServiceImpl()) // Add the service to the server.
            .intercept(new ServerInterceptor()) // Add the interceptor to the server
            .build(); // Build the server.

    /**
     * Helper method to get the file from the path.
     * @param fileName The file name to get.
     * @return The file.
     */
    private static File getFile(final String fileName) {
        return new File(Objects.requireNonNull(ReportServer.class.getResource(fileName)).getFile());
    }

    /**
     * Starts the server by calling the super class run method.
     */
    public void run() {
        try {
            super.run(reportServer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
