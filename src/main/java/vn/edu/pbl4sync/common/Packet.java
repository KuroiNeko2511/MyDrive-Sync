package vn.edu.pbl4sync.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Packet {
    private final String type;
    private final String requestId;
    private final Map<String, String> data;

    public Packet(String type, String requestId, Map<String, String> data) {
        this.type = type;
        this.requestId = requestId == null ? "" : requestId;
        this.data = data == null ? new LinkedHashMap<>() : new LinkedHashMap<>(data);
    }

    public static Packet request(String type) {
        return new Packet(type, UUID.randomUUID().toString(), null);
    }

    public static Packet event(String type) {
        return new Packet(type, "", null);
    }

    public Packet with(String key, Object value) {
        data.put(key, value == null ? "" : String.valueOf(value));
        return this;
    }

    public String type() { return type; }
    public String requestId() { return requestId; }
    public Map<String, String> data() { return data; }
    public String get(String key) { return data.getOrDefault(key, ""); }
    public int getInt(String key, int fallback) {
        try { return Integer.parseInt(get(key)); } catch (Exception e) { return fallback; }
    }
    public long getLong(String key, long fallback) {
        try { return Long.parseLong(get(key)); } catch (Exception e) { return fallback; }
    }
    public boolean getBoolean(String key, boolean fallback) {
        String v = get(key);
        return v.isBlank() ? fallback : Boolean.parseBoolean(v);
    }

    public Packet response(String responseType) {
        return new Packet(responseType, requestId, null);
    }

    @Override
    public String toString() {
        return "Packet{" + type + ", requestId='" + requestId + "', data=" + data + "}";
    }
}
