package vn.edu.pbl4sync.server;

import vn.edu.pbl4sync.server.ui.ServerMonitorFrame;

import javax.swing.*;
import java.nio.file.Path;

public class ServerMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerConfig config = ServerConfig.load(Path.of("config", "server.properties"));
            ServerCore server = new ServerCore(config);
            new ServerMonitorFrame(server).setVisible(true);
        });
    }
}
