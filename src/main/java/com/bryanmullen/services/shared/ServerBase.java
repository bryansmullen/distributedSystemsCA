package com.bryanmullen.services.shared;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.mdns.PropertiesReader;
import com.bryanmullen.mdns.ServiceRegistration;
import io.grpc.BindableService;
import io.grpc.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.util.Properties;

/**
 * ServiceBase is the base class for the services. This class will be used to create the implementation of the server
 * classes.
 */
public abstract class ServerBase {
    // instance variables

    // logger for this class
    Logger logger = LoggerFactory.getLogger(ClientInterceptor.class);
    Properties properties;
    BindableService serviceImpl;
    String serverName;

    // constructor
    public ServerBase(String propertiesFilePath, BindableService bindableService) throws IOException {
        // get the properties from the properties file.
        properties = PropertiesReader.getProperties(propertiesFilePath);

        // set the server name from the properties file.
        setServerName(properties.getProperty("service_name"));

        // set the service implementation to the bindable service passed in.
        serviceImpl = bindableService;
    }


    // methods

    /**
     * run - This method will run the server. It will be called by the implementing class.
     *
     * @param server - the server to run.
     */
    public void run(Server server) throws InterruptedException {


        // start the server
        try {
            server.start();
        } catch (IOException e) {
            logger.error("Server failed to start." + e.getMessage());
        }

        // log that the server has started
        logger.info("Server started on port " + properties.getProperty("port"));

        // register the service with the network
        var serviceRegistration = new ServiceRegistration(properties);
        serviceRegistration.register();

        // listen for requests
        serverListen(server, serviceRegistration.getJmDNS(), serviceRegistration.getServiceInfo());
    }

    /**
     * serverListen - This method will listen for requests on the server.
     *
     * @param server - the server to listen on.
     * @param mdns - the jmdns instance to use.
     * @param serviceInfo - the service information to use.
     */
    private void serverListen(io.grpc.Server server, JmDNS mdns,
                              ServiceInfo serviceInfo) {
        // shutdown hook to stop the server gracefully.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // log that the server is shutting down
            logger.info("Shutting down gRPC server " + serverName + "..."); // log that the server is shutting down

            // unregister the service
            logger.info("Unregistering service " + serviceInfo.getName() + "..."); // log that the service is unregistering
            mdns.unregisterService(serviceInfo); // unregister the service
            logger.info("Service unregistered."); // log that the service has been unregistered

            // close the jMDNS instance
            try {
                mdns.close(); // close the jMDNS instance
                logger.info("jMDNS closed."); // log that the jMDNS instance has been closed.
            } catch (IOException e) {
                // log that the jMDNS instance failed to close.
                logger.error("jMDNS failed to close." + e.getMessage());
                throw new RuntimeException(e);
            }

            // shutdown the server
            server.shutdown(); // shutdown the server
            logger.info("Server shutdown."); // log that the server has been shutdown.
        }));

        // block until the server is terminated

        try {
            // inform the user that the server is ready
            logger.info("Server " + serverName + " is listening for Requests..."); // log that the server is ready

            // block until the server is terminated
            server.awaitTermination();
        } catch (InterruptedException e) {
            // log that the server was interrupted
            logger.error("Server " + serverName + " was interrupted." + e.getMessage());
        }
    }

    // getters and setters
    public Properties getProperties() {
        return properties;
    }

    private void setServerName(String service_name) {
        this.serverName = service_name;
    }

}
