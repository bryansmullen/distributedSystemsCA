package com.bryanmullen.services.report.server;

import com.bryanmullen.interceptors.ServerInterceptor;
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
            // .useTransportSecurity(new File("src/ssl/server.crt"), new File("src/ssl/server.pem")) TODO: Troubleshoot why tls key is not correctly read in on client side before enabling this
            .addService(new ReportServiceImpl())
            .intercept(new ServerInterceptor())
            .build();


    public void run() {
        try {
            super.run(reportServer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
