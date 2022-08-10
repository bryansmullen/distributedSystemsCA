package com.bryanmullen.mdns;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class ServiceRegistration {
    JmDNS jmDNS;
    ServiceInfo serviceInfo;

    public ServiceRegistration(Properties properties) {
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

    public void register()  {

        try {
            // Create a JmDNS instance
            jmDNS = JmDNS.create(InetAddress.getLocalHost());
            System.out.println("Registering service " + serviceInfo.getName());
            jmDNS.registerService(serviceInfo);
            System.out.println("Service Registered " + serviceInfo.getName());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public JmDNS getJmDNS() {
        return jmDNS;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }
}