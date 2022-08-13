package com.bryanmullen.services.client.gui.panels;
import com.bryanmullen.services.shared.ClientBase;
import java.io.IOException;

public abstract class PanelBase  extends ClientBase {
    public PanelBase(String propertiesFilePath) throws IOException {
        super(propertiesFilePath);
    }



}
