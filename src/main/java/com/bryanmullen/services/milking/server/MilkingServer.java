package com.bryanmullen.services.milking.server;


import com.bryanmullen.interceptors.ServerInterceptor;
import com.bryanmullen.services.shared.ServerBase;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * The MilkingServer class is the server that will be used to serve the milking service. This server will be responsible for
 * initializing the server, mounting the services, implementing the interceptors, whether to use SSL, and finally starting the server.
 */
public class MilkingServer extends ServerBase {
    // constructor
    public MilkingServer(String propertiesFilePath, BindableService bindableService) throws IOException {
        // Call the super class constructor.
        super(propertiesFilePath, bindableService);
    }

    // Build the server
    Server milkingServer = ServerBuilder
            .forPort(Integer.parseInt(getProperties().getProperty( // The port that the server will be listening on.
                    "service_port"))) // Get the port from the properties file
            .useTransportSecurity( // useTransportSecurity will determine whether to use SSL.
                    getFile("/my-public-key-cert.pem"), // public Key
                    getFile("/my-private-key.pem")) // private key
            .addService(new MilkingServiceImpl()) // Add the service to the server.
            .intercept(new ServerInterceptor()) // Add the interceptor to the server
            .build(); // Build the server.

    /**
     * Helper method to get the file from the path.
     * @param fileName The file name to get.
     * @return The file.
     */
    private static File getFile(final String fileName) {
        return new File(Objects.requireNonNull(MilkingServer.class.getResource(fileName)).getFile());
    }

    /**
     * Starts the server by calling the super class run method.
     */
    public void run() {
        try {
            super.run(milkingServer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
