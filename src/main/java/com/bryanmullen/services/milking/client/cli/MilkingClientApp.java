package com.bryanmullen.services.milking.client.cli;

import java.io.IOException;

public class MilkingClientApp {
    public static void main(String[] args) throws IOException {
        var milkingClient = new MilkingClient("src/main/resources/milking" +
                ".properties");

        milkingClient.getService();
        milkingClient.milkCollection();
        milkingClient.milkProduction();
        milkingClient.milkCurrentCow();
    }
}
