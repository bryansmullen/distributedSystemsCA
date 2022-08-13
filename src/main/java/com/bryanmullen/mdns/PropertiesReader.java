package com.bryanmullen.mdns;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Properties reader - this class allows for the reading of properties from a file. This is useful for defining the grpc
 * service properties including service name, port, type and description.
 */
public class PropertiesReader {
    /**
     * getProperties - this method reads the properties from the specified file and returns them as a Properties
     * object.
     *
     * @param filePath The path of the file to read from.
     * @return The properties read from the file.
     */
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
