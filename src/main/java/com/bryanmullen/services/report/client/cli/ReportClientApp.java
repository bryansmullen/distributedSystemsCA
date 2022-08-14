package com.bryanmullen.services.report.client.cli;

import java.io.IOException;

/**
 * ReportClientApp - This class is the main class for the report client application.
 */
public class ReportClientApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        // create a new report client object and pass in the properties file path
        var reportClient = new ReportClient("src/main/resources/report.properties");

        // get the service info from the service discovery
        reportClient.getService();

        // call the remote procedures calls
        reportClient.cowReport(1, "Bryan");
        reportClient.herdReport(1, "Bryan");

    }

}
