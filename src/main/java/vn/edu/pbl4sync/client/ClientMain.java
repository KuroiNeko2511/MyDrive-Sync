package vn.edu.pbl4sync.client;

import vn.edu.pbl4sync.client.ui.LoginFrame;

import javax.swing.*;
import java.nio.file.Path;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientConfig config = new ClientConfig(Path.of("config", "client.properties"));
            new LoginFrame(config).setVisible(true);
        });
    }
}
