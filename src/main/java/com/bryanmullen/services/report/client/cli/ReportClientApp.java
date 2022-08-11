package com.bryanmullen.services.report.client.cli;

import java.io.IOException;

public class ReportClientApp {
    public static void main(String[] args) throws IOException {

        var reportClient = new ReportClient("src/main/resources/report.properties");
        reportClient.cowReport();
        reportClient.herdReport();
    }

}