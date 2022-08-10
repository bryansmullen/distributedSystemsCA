package com.bryanmullen.services.milking.client.cli;
import com.bryanmullen.services.shared.ClientBase;
import java.io.IOException;

public class MilkingClient extends ClientBase {
    public MilkingClient(String propertiesFilePath) throws IOException {
        super(propertiesFilePath);
    }

    public void milkCollection() {
        // inform the user of the start of the call
        System.out.println("Starting to do Milk Collection method...");
        closeChannel();
    }

    public void milkProduction() {
        // inform the user of the start of the call
        System.out.println("Starting to do Milk Production method...");
        super.closeChannel();

    }

    public void milkCurrentCow() {
        // inform the user of the start of the call
        System.out.println("Starting to do Milk Current Cow method...");
        super.closeChannel();

    }


}
