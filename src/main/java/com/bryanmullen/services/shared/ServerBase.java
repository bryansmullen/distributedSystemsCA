package com.bryanmullen.services.shared;

import com.bryanmullen.mdns.PropertiesReader;
import com.bryanmullen.mdns.ServiceRegistration;
import io.grpc.BindableService;
import io.grpc.Server;


import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.util.Properties;

public abstract class ServerBase {
    // instance variables
    Properties properties;
    BindableService serviceImpl;


    // constructor
    public ServerBase(String propertiesFilePath, BindableService bindableService) throws IOException {
        properties = PropertiesReader.getProperties(propertiesFilePath);
        serviceImpl = bindableService;
    }

    // methods
    public void run(Server server)  {
        // constants
        startServer(server);

        var serviceRegistration = new ServiceRegistration(properties);
        try {
            serviceRegistration.register();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        serverListen(server, serviceRegistration.getJmDNS(), serviceRegistration.getServiceInfo());
    }

    private void startServer(Server server) {

        // start the server
        try {
            server.start();
        } catch (IOException e) {
            System.out.println("Failed to start server: " + e.getMessage());
        }

        System.out.println("Server started on port " + properties.getProperty("service_port"));
    }

    private static void serverListen(io.grpc.Server server, JmDNS mdns,
                                     ServiceInfo serviceInfo) {
        // shutdown hook to stop the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down milking server...");
            // unregister the service
            mdns.unregisterService(serviceInfo);
            System.out.println("Service Unregistered");

            // close the jMDNS instance
            try {
                mdns.close();

                System.out.println("jMDNS closed");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // shutdown the server
            server.shutdown();
            System.out.println("Server shut down");
        }));

        // block until the server is terminated

        try {
            // inform the user that the server is ready
            System.out.println("Milking Server Listening for Requests...");

            server.awaitTermination();
        } catch (InterruptedException e) {
            System.out.println("Failed to await termination: " + e.getMessage());
        }
    }

    // getters and setters
    public Properties getProperties() {
        return properties;
    }

}
