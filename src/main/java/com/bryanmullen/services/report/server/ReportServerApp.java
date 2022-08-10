package com.bryanmullen.services.report.server;

import java.io.IOException;

public class ReportServerApp {
    public static void main(String[] args) throws IOException {
        var reportServer = new ReportServer("src/main/resources/report.properties", new ReportServiceImpl());
        reportServer.run();
    }
}
