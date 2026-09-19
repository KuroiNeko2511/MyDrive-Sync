package vn.edu.pbl4sync.client.sync;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Base64;
import java.util.Properties;

public class LocalStateStore {
    private final Path file;
    private final Properties p = new Properties();

    public LocalStateStore(Path root) {
        this.file = root.resolve(".pbl4sync-state.properties");
        load();
    }

    private synchronized void load() {
        if (!Files.exists(file)) return;
        try (InputStream in = Files.newInputStream(file)) { p.load(in); } catch (Exception ignored) { }
    }

    private String key(long ws, String path) {
        String raw = ws + "|" + path.replace('\\', '/');
        return Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public synchronized long version(long ws, String path) {
        String value = p.getProperty(key(ws, path), "0|");
        try { return Long.parseLong(value.split("\\|", 2)[0]); } catch (Exception e) { return 0; }
    }

    public synchronized String checksum(long ws, String path) {
        String value = p.getProperty(key(ws, path), "0|");
        String[] parts = value.split("\\|", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    public synchronized void put(long ws, String path, long version, String checksum) {
        p.setProperty(key(ws, path), version + "|" + (checksum == null ? "" : checksum));
        save();
    }

    public synchronized void remove(long ws, String path) {
        p.remove(key(ws, path));
        save();
    }

    private void save() {
        try {
            Files.createDirectories(file.getParent());
            try (OutputStream out = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                p.store(out, "PBL4 Sync local file versions");
            }
        } catch (Exception ignored) { }
    }
}
