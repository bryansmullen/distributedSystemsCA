package com.bryanmullen.mdns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

/**
 * ServiceRegistration is a class that allows for the registration of a service on the local network. It is used to
 * create a service on the local network that can be discovered by other devices. The service is registered with a name,
 * type, and port. The service is then published on the local network using the JmDNS library.
 */
public class ServiceRegistration {
    // Instance variables
    Logger logger = LoggerFactory.getLogger(ServiceRegistration.class); // Logger for this class.
    JmDNS jmDNS;
    ServiceInfo serviceInfo;

    // Constructor
    public ServiceRegistration(Properties properties) {
        // Create a new ServiceInfo object using the properties passed in through the constructor
        final String SERVICE_TYPE = properties.getProperty(
                "service_type");
        final String SERVICE_NAME = properties.getProperty(
                "service_name");
        final String SERVICE_DESCRIPTION = properties.getProperty(
                "service_description");
        final int SERVICE_PORT = Integer.parseInt(properties.getProperty(
                "service_port"));

        serviceInfo = ServiceInfo.create(
                SERVICE_TYPE,
                SERVICE_NAME,
                SERVICE_PORT,
                SERVICE_DESCRIPTION);
    }


    // Methods

    /**
     * Register the service on the local network.
     */
    public void register() {

        try {
            // Create a JmDNS instance using the local host address
            jmDNS = JmDNS.create(InetAddress.getLocalHost());

            // Register the service with the JmDNS instance and log the start and end of the registration
            logger.info("Registering service: " + serviceInfo.getName());
            jmDNS.registerService(serviceInfo);
            logger.info("Service Registered " + serviceInfo.getName());

        } catch (IOException e) {
            logger.error("Service Registration failed: " + e.getMessage());
        }
    }

    // getters and setters
    public JmDNS getJmDNS() {
        return jmDNS;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }
}