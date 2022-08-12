package com.bryanmullen.services.report.client.cli;

import com.bryanmullen.reportService.CowReportRequest;
import com.bryanmullen.reportService.ReportServiceGrpc;
import com.bryanmullen.services.shared.ClientBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ReportClient extends ClientBase {
    Logger logger = LoggerFactory.getLogger(ReportClient.class); //
    // Logger for this class so we can log messages to the console.


    public ReportClient(String propertiesFilePath) throws IOException {
        super(propertiesFilePath);
    }

    public void cowReport() {
        // log the start of the call
        logger.info("Starting to do Cow Report method...");

        // create the client stub for the service
        var stub = ReportServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        var response =
                stub.cowReport(CowReportRequest
                        .newBuilder()
                        .setCowId(1)
                        .setCheckedBy("Bryan")
                        .build());

        // print the response to the console
        System.out.println("Cow Report Response: " + response);

        // close the channel
        super.closeChannel();

    }

    public void herdReport() {
        System.out.println("Starting to do Herd Report method...");

    }

}
