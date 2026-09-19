package vn.edu.pbl4sync.client.sync;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IgnoreRegistry {
    private final Map<Path, Long> until = new ConcurrentHashMap<>();

    public void ignore(Path path, long millis) {
        until.put(path.toAbsolutePath().normalize(), System.currentTimeMillis() + millis);
    }

    public boolean shouldIgnore(Path path) {
        Path p = path.toAbsolutePath().normalize();
        Long expiry = until.get(p);
        if (expiry == null) return false;
        if (expiry < System.currentTimeMillis()) {
            until.remove(p);
            return false;
        }
        return true;
    }
}
