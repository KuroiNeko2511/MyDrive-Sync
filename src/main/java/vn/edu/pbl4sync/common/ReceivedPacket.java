package vn.edu.pbl4sync.common;

import java.nio.file.Path;

public record ReceivedPacket(Packet packet, Path payloadPath, long payloadLength) implements AutoCloseable {
    @Override
    public void close() {
        if (payloadPath != null) {
            try { java.nio.file.Files.deleteIfExists(payloadPath); } catch (Exception ignored) { }
        }
    }
}
