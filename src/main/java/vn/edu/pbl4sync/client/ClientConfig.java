package vn.edu.pbl4sync.client;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.*;
import java.util.Properties;

public class ClientConfig {
    private final Path file;
    private final Properties props = new Properties();

    public ClientConfig(Path file) {
        this.file = file;
        load();
    }

    private void load() {
        if (Files.exists(file)) {
            try (InputStream in = Files.newInputStream(file)) { props.load(in); } catch (Exception ignored) { }
        }
        props.putIfAbsent("server.host", "127.0.0.1");
        props.putIfAbsent("server.port", "5000");
        props.putIfAbsent("sync.root", Path.of(System.getProperty("user.home"), "CompanySync").toString());
        try { props.putIfAbsent("device.name", InetAddress.getLocalHost().getHostName()); }
        catch (Exception e) { props.putIfAbsent("device.name", "Java-Agent"); }
    }

    public String host() { return props.getProperty("server.host"); }
    public int port() { return Integer.parseInt(props.getProperty("server.port")); }
    public Path syncRoot() { return Path.of(props.getProperty("sync.root")).toAbsolutePath().normalize(); }
    public String deviceName() { return props.getProperty("device.name"); }

    public void setHost(String v) { props.setProperty("server.host", v); }
    public void setPort(int v) { props.setProperty("server.port", String.valueOf(v)); }
    public void setSyncRoot(Path v) { props.setProperty("sync.root", v.toAbsolutePath().normalize().toString()); }

    public void save() throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());
        try (OutputStream out = Files.newOutputStream(file)) {
            props.store(out, "PBL4 Sync client settings");
        }
    }
}
