package com.bryanmullen.mdns;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    public static Properties getProperties(String filePath) throws IOException {

        // Load a properties file from the path specified
        InputStream fileInputStream = new FileInputStream(filePath);

        // Create a properties object
        Properties serviceProperties = new Properties();

        // load the properties file into the properties object
        serviceProperties.load(fileInputStream);

        // Print the properties to the console
        System.out.println("Loading properties...");
        System.out.println("\t service_type: " + serviceProperties.getProperty("service_type"));
        System.out.println("\t service_name: " + serviceProperties.getProperty("service_name"));
        System.out.println("\t service_description: " + serviceProperties.getProperty("service_description"));
        System.out.println("\t service_port: " + serviceProperties.getProperty("service_port"));

        // Close the input stream
        fileInputStream.close();

        // return the properties object
        return serviceProperties;
    }
}
