package com.bryanmullen.services.shared;

import com.bryanmullen.mdns.PropertiesReader;
import com.bryanmullen.mdns.ServiceDiscovery;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.util.Properties;

public abstract class ClientBase {
    Properties properties;
    ManagedChannel channel;
    ServiceInfo serviceInfo;

    public ClientBase(String propertiesFilePath) throws IOException {
        properties = PropertiesReader.getProperties(propertiesFilePath);
    }

    public void getService() {
        // find service on the network
        String SERVICE_TYPE = properties.getProperty("service_type");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

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

    private void setChannel(String host, int port) {
        channel = ManagedChannelBuilder.
                forAddress(host, port)
                .usePlaintext()
                .build();
    }


}
