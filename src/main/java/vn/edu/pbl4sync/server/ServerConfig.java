package vn.edu.pbl4sync.server;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public record ServerConfig(int port, String dbUrl, String dbUser, String dbPassword, String initialAdminPassword, int dbPoolSize, int clientThreads, int transferThreads) {
    public static ServerConfig load(Path path) {
        Properties p = new Properties();
        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path)) { p.load(in); } catch (Exception ignored) { }
        }
        return new ServerConfig(
                Integer.parseInt(p.getProperty("server.port", "5000")),
                p.getProperty("db.url", "jdbc:mysql://localhost:3306/pbl4sync?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh"),
                p.getProperty("db.user", "root"),
                p.getProperty("db.password", "123456"),
                p.getProperty("admin.initialPassword", "admin123"),
                Integer.parseInt(p.getProperty("db.poolSize", "10")),
                Integer.parseInt(p.getProperty("server.clientThreads", "150")),
                Integer.parseInt(p.getProperty("server.transferThreads", "10"))
        );
    }
}
