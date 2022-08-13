package com.bryanmullen.mdns;

import java.io.IOException;
import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

public class ServiceDiscovery {
    //public static void main(String[] args) {
    public static ServiceInfo run(String serviceType) {

        ServiceInfo serviceInfo = null;
        // get an instance of jmDNS
        
        try {
            // get an instance of jmDNS. Don't ask why, but the IP address
            // has to be manually inputted on my home network, my phone
            // tethering, and any other network i tested on except for my
            // work network. You will need to enter your own IP address here
            // or replace with InetAddress.getLocalHost().
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

            // will discover the service based on service type
            MyServiceListener serviceListener = new MyServiceListener();
            jmdns.addServiceListener(serviceType, serviceListener);


            // sleep for 10 seconds - this is the time it takes for the
            // service to be discovered. This may be better implemented with
            // a future object?
            Thread.sleep(1000);

            // get the service info from the service listener
            serviceInfo = serviceListener.getServiceInfo();

            // close the jmdns instance - this is important as it will stop the
            // discovery of services.
            jmdns.close();


        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // return serviceInfo to the caller or null if no service found.
        if (serviceInfo == null) {
            System.out.println("No service found");
            return null;
        }
        return serviceInfo;

    }
    private static class MyServiceListener implements ServiceListener {
        // instance variables
        private ServiceInfo serviceInfo;

        // methods - implement the service listener interface methods
        public void serviceAdded(ServiceEvent event) {
            // TODO Auto-generated method stub
            System.out.println("\nService Added " + event.getInfo());
        }

        public void serviceRemoved(ServiceEvent event) {
            // TODO Auto-generated method stub
            System.out.println("Service Removed " + event.getInfo());

        }

        public void serviceResolved(ServiceEvent event) {
            ServiceInfo serviceInfo = event.getInfo();
            System.out.println("Service Resolved " + event.getInfo());

            // set the hostname and port of the service listener
            setServiceInfo(serviceInfo);
        }

        // getters and setters
        public ServiceInfo getServiceInfo() {
            return serviceInfo;
        }

        public void setServiceInfo(ServiceInfo serviceInfo) {
            this.serviceInfo = serviceInfo;
        }

    }
}
