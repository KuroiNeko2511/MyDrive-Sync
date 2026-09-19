package vn.edu.pbl4sync.server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionManager {
    private final Map<Long, ClientSession> byAgent = new ConcurrentHashMap<>();
    private final Map<Long, CopyOnWriteArrayList<ClientSession>> byUser = new ConcurrentHashMap<>();

    public void add(ClientSession s) {
        byAgent.put(s.agentId(), s);
        byUser.computeIfAbsent(s.userId(), k -> new CopyOnWriteArrayList<>()).addIfAbsent(s);
    }

    public void remove(ClientSession s) {
        if (s.agentId() != 0) byAgent.remove(s.agentId(), s);
        var list = byUser.get(s.userId());
        if (list != null) {
            list.remove(s);
            if (list.isEmpty()) byUser.remove(s.userId());
        }
    }

    public ClientSession byAgent(long agentId) { return byAgent.get(agentId); }
    public List<ClientSession> byUser(long userId) { return List.copyOf(byUser.getOrDefault(userId, new CopyOnWriteArrayList<>())); }
    public int onlineCount() { return byAgent.size(); }
    public Collection<ClientSession> all() { return List.copyOf(byAgent.values()); }
}
