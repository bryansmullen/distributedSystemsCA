package com.bryanmullen.services.milking.server;


import com.bryanmullen.interceptors.ServerInterceptor;
import com.bryanmullen.services.shared.ServerBase;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class MilkingServer extends ServerBase {
    public MilkingServer(String propertiesFilePath, BindableService bindableService) throws IOException {
        super(propertiesFilePath, bindableService);
    }

    Server milkingServer = ServerBuilder
            .forPort(Integer.parseInt(getProperties().getProperty(
                    "service_port")))
            // .useTransportSecurity(new File("src/ssl/server.crt"), new File("src/ssl/server.pem")) TODO: Troubleshoot why tls key is not correctly read in on client side before enabling this
            .addService(new MilkingServiceImpl())
            .intercept(new ServerInterceptor())
            .build();


    public void run() {
        try {
            super.run(milkingServer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
