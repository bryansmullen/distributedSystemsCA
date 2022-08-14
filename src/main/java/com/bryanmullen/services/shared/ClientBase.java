package com.bryanmullen.services.shared;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.mdns.PropertiesReader;
import com.bryanmullen.mdns.ServiceDiscovery;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.ServiceInfo;
import javax.net.ssl.SSLException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public abstract class ClientBase {
    // instance variables

    // logger for this class
    Logger logger = LoggerFactory.getLogger(ClientInterceptor.class);
    Properties properties;
    ManagedChannel channel;
    ServiceInfo serviceInfo;

    // constructor
    public ClientBase(String propertiesFilePath) throws IOException {
        properties = PropertiesReader.getProperties(propertiesFilePath); // Get the properties from the properties file.
    }

    // methods

    /**
     * getService - This service will get the service type from the properties file. It will then run the service
     * discovery to find the service information before creating the channel using the service information.
     *
     */
    public void getService() throws SSLException {
        // find service on the network
        String SERVICE_TYPE = properties.getProperty("service_type");

        // discover the service
        serviceInfo = ServiceDiscovery.run(SERVICE_TYPE);

        // in the case that the service is not found, log the error.
        if (serviceInfo == null) {
            logger.error("Service not found.");
        } else {
            // pull data from the service info
            String HOST = serviceInfo.getHostAddresses()[0];
            int PORT = serviceInfo.getPort();

            // log that service has been found
            logger.info("Service found at " + HOST + ":" + PORT);

            // create the channel
            setChannel(HOST, PORT);
        }
    }

    /**
     * closeChannel - This method will close the channel which allows for graceful shutdown of the channel.
     */
    public void closeChannel() {
        logger.info("Closing channel..."); // Log that the channel is being closed.
        channel.shutdown(); // Close the channel.
        logger.info("Channel closed."); // Log that the channel has been closed.
    }

    // setters and getters
    public ManagedChannel getChannel() {
        return channel;
    }

    private void setChannel(String host, int port) throws SSLException {
        // create the channel using NettyChannelBuilder - this allows us to use SSL
        channel = NettyChannelBuilder.forAddress("localhost", port) // Set the host and port.
                .sslContext(GrpcSslContexts // Set the SSL context.
                        .forClient() // For the client.
                        .trustManager(getFile()) // public key
                        .build()) // build the SSL context.
                .build(); // build the channel.
    }


    /**
     * getFile - This method will get the file from the path.
     *
     * @return The file.
     */
    private static File getFile() {
        return new File(Objects.requireNonNull(ClientBase
                        .class
                        .getResource("/my-public-key-cert.pem")) // Get the file from the path. - this is the public key.
                .getFile());
    }

}
