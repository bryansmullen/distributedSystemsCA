package com.bryanmullen.services.milking.server;


import com.bryanmullen.interceptors.ServerInterceptor;
import com.bryanmullen.services.shared.ServerBase;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MilkingServer extends ServerBase {
    public MilkingServer(String propertiesFilePath, BindableService bindableService) throws IOException {
        super(propertiesFilePath, bindableService);
    }

    Server milkingServer = ServerBuilder
            .forPort(Integer.parseInt(getProperties().getProperty(
                    "service_port")))
            .useTransportSecurity(
                    getFile("/my-public-key-cert.pem"), //public Key
                    getFile("/my-private-key.pem")) // private key            .addService(new MilkingServiceImpl())
            .addService(new MilkingServiceImpl())
            .intercept(new ServerInterceptor())
            .build();

    private static File getFile(final String fileName) {
        return new File(Objects.requireNonNull(MilkingServer.class.getResource(fileName)).getFile());
    }

    public void run() {
        try {
            super.run(milkingServer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
