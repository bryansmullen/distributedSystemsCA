package com.bryanmullen.services.milking.server;


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
            .addService(new MilkingServiceImpl())
            .build();


    public void run() {
        try {
            super.run(milkingServer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
