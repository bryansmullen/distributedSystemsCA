package com.bryanmullen.services.report.client.cli;

import com.bryanmullen.services.shared.ClientBase;

import java.io.IOException;

public class ReportClient extends ClientBase {


    public ReportClient(String propertiesFilePath) throws IOException {
        super(propertiesFilePath);
    }

    public void cowReport() {
        System.out.println("Starting to do Cow Report method...");

    }

    public void herdReport() {
        System.out.println("Starting to do Herd Report method...");

    }

}
