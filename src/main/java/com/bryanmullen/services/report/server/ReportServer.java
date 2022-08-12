package com.bryanmullen.services.report.server;

import com.bryanmullen.services.shared.ServerBase;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ReportServer extends ServerBase {
    public ReportServer(String propertiesFilePath, BindableService bindableService) throws IOException {
        super(propertiesFilePath, bindableService);
    }

    Server reportServer = ServerBuilder
            .forPort(Integer.parseInt(getProperties().getProperty(
                    "service_port")))
            .addService(new ReportServiceImpl())
            .build();


    public void run() {
        try {
            super.run(reportServer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
