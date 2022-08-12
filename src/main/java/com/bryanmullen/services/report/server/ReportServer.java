package com.bryanmullen.services.report.server;

import com.bryanmullen.services.shared.ServerBase;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    static class ServerInterceptor implements io.grpc.ServerInterceptor {
        Logger logger = LoggerFactory.getLogger(ServerInterceptor.class); //

        @Override
        public <ReqT, RespT> io.grpc.ServerCall.Listener<ReqT> interceptCall(io.grpc.ServerCall<ReqT, RespT> call, io.grpc.Metadata headers, io.grpc.ServerCallHandler<ReqT, RespT> next) {
            logger.info("Received the following headers: " + headers);
            return next.startCall(call, headers);
        }
    }
}
