package com.bryanmullen.services.shared;

import com.bryanmullen.mdns.PropertiesReader;
import com.bryanmullen.mdns.ServiceDiscovery;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;

import javax.jmdns.ServiceInfo;
import javax.net.ssl.SSLException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public abstract class ClientBase {
    Properties properties;
    ManagedChannel channel;
    ServiceInfo serviceInfo;

    public ClientBase(String propertiesFilePath) throws IOException {
        properties = PropertiesReader.getProperties(propertiesFilePath);
    }

    public void getService() throws SSLException {
        // find service on the network
        String SERVICE_TYPE = properties.getProperty("service_type");

        serviceInfo = ServiceDiscovery.run(SERVICE_TYPE);

        if (serviceInfo == null) {
            System.out.println("No service found on the network");
        } else {
            String HOST = serviceInfo.getHostAddresses()[0];
            int PORT = serviceInfo.getPort();

            // log that service has been found
            System.out.println("Found service at " + HOST + ":" + PORT);

            // create the channel
            setChannel(HOST, PORT);
        }
    }

    public void closeChannel() {
        System.out.println("Closing channel...");
        channel.shutdown();
        System.out.println("Channel closed");
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    private void setChannel(String host, int port) throws SSLException {

        channel = NettyChannelBuilder.forAddress("localhost", port)
                .sslContext(GrpcSslContexts
                        .forClient()
                        .trustManager(getFile()) // public key
                        .build())
                .build();
    }


    private static File getFile() {
        return new File(Objects.requireNonNull(ClientBase
                        .class
                        .getResource("/my-public-key-cert.pem"))
                .getFile());
    }

}
