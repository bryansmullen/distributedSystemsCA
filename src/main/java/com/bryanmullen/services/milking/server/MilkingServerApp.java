package com.bryanmullen.services.milking.server;

import java.io.IOException;

public class MilkingServerApp {
    public static void main(String[] args) throws IOException {
        var milkingServer = new MilkingServer("src/main/resources/milking.properties", new MilkingServiceImpl());
        milkingServer.run();
    }
}
