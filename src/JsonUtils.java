import java.util.*;

/**
 * Lightweight JSON utility for parsing and generating JSON without external libraries.
 */
public class JsonUtils {

    public static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < ' ') {
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /**
     * Parses simple flat JSON object {"key": "val", "num": 123, "bool": true}
     */
    public static Map<String, String> parseJsonObject(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null) return map;
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        // Simple tokenizer for key-value pairs
        boolean inQuote = false;
        StringBuilder currentKey = new StringBuilder();
        StringBuilder currentValue = new StringBuilder();
        boolean readingKey = true;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '\\' && i + 1 < json.length()) {
                if (readingKey) currentKey.append(json.charAt(i + 1));
                else currentValue.append(json.charAt(i + 1));
                i++;
                continue;
            }

            if (c == '"') {
                inQuote = !inQuote;
                continue;
            }

            if (!inQuote) {
                if (c == ':') {
                    readingKey = false;
                    continue;
                }
                if (c == ',') {
                    String k = currentKey.toString().trim();
                    String v = currentValue.toString().trim();
                    if (!k.isEmpty()) {
                        map.put(k, v);
                    }
                    currentKey = new StringBuilder();
                    currentValue = new StringBuilder();
                    readingKey = true;
                    continue;
                }
            }

            if (readingKey) {
                currentKey.append(c);
            } else {
                currentValue.append(c);
            }
        }

        String k = currentKey.toString().trim();
        String v = currentValue.toString().trim();
        if (!k.isEmpty()) {
            map.put(k, v);
        }

        return map;
    }
}
