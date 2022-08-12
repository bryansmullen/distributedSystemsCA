package com.bryanmullen.services.report.client.gui;

import java.io.IOException;

public class ReportGuiClientApp {
    public static void main(String[] args) throws IOException {

        var reportClient = new ReportGuiClient("src/main/resources/report.properties");
        reportClient.getService();
        reportClient.run();

    }

}
