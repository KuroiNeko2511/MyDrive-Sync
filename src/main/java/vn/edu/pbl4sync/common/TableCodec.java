package vn.edu.pbl4sync.common;

import java.io.*;
import java.util.*;
import java.util.Base64;

public final class TableCodec {
    private TableCodec() {}

    public static String encode(List<Map<String, String>> rows) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (DataOutputStream out = new DataOutputStream(baos)) {
                out.writeInt(rows.size());
                for (Map<String, String> row : rows) {
                    out.writeInt(row.size());
                    for (Map.Entry<String, String> e : row.entrySet()) {
                        ProtocolIO.writeString(out, e.getKey());
                        ProtocolIO.writeString(out, e.getValue() == null ? "" : e.getValue());
                    }
                }
            }
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<Map<String, String>> decode(String encoded) {
        if (encoded == null || encoded.isBlank()) return new ArrayList<>();
        try {
            byte[] bytes = Base64.getDecoder().decode(encoded);
            try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
                int rows = in.readInt();
                if (rows < 0 || rows > 100_000) throw new IOException("Invalid row count");
                List<Map<String, String>> result = new ArrayList<>(rows);
                for (int i = 0; i < rows; i++) {
                    int cols = in.readInt();
                    Map<String, String> row = new LinkedHashMap<>();
                    for (int c = 0; c < cols; c++) {
                        row.put(ProtocolIO.readString(in), ProtocolIO.readString(in));
                    }
                    result.add(row);
                }
                return result;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot decode table", e);
        }
    }
}
