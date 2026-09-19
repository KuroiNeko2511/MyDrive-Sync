package vn.edu.pbl4sync.server;

import vn.edu.pbl4sync.common.Packet;
import vn.edu.pbl4sync.common.ProtocolIO;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;

public class ClientSession {
    private final Socket socket;
    private final Object sendLock = new Object();
    private long userId;
    private long agentId;
    private String username = "";
    private String systemRole = "USER";
    private String deviceName = "";
    private volatile boolean authenticated;

    public ClientSession(Socket socket) { this.socket = socket; }

    public void send(Packet packet) throws IOException { send(packet, null); }
    public void send(Packet packet, Path payload) throws IOException {
        synchronized (sendLock) {
            ProtocolIO.send(socket.getOutputStream(), packet, payload);
        }
    }

    public Socket socket() { return socket; }
    public long userId() { return userId; }
    public long agentId() { return agentId; }
    public String username() { return username; }
    public String systemRole() { return systemRole; }
    public String deviceName() { return deviceName; }
    public boolean authenticated() { return authenticated; }
    public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(systemRole); }

    public void authenticate(long userId, long agentId, String username, String systemRole, String deviceName) {
        this.userId = userId;
        this.agentId = agentId;
        this.username = username;
        this.systemRole = systemRole;
        this.deviceName = deviceName;
        this.authenticated = true;
    }

    public boolean isOpen() { return !socket.isClosed(); }
    public void close() { try { socket.close(); } catch (IOException ignored) { } }
}
