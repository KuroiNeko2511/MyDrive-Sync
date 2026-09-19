package vn.edu.pbl4sync.server;

@FunctionalInterface
public interface ServerLogListener {
    void onLog(String message);
}
