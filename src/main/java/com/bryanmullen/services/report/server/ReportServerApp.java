package com.bryanmullen.services.report.server;

import java.io.IOException;

/**
 * ReportServerApp is the main class for the ReportServer.
 */
public class ReportServerApp {
    public static void main(String[] args) throws IOException {
        // Create a new ReportServer and pass in the properties file path and the service to bind.
        var reportServer = new ReportServer("src/main/resources/report.properties", new ReportServiceImpl());

        // Run the server.
        reportServer.run();
    }
}
