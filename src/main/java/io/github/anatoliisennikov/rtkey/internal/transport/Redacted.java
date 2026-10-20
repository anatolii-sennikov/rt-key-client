package io.github.anatoliisennikov.rtkey.internal.transport;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

final class Redacted {
    private Redacted() {
    }

    public static String redact(URI uri) {
        StringBuilder sb = new StringBuilder();
        sb.append(uri.getScheme()).append("://").append(uri.getAuthority()).append(uri.getRawPath());
        String query = uri.getRawQuery();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&", -1);
            List<String> filtered = new ArrayList<>();
            for (String pair : pairs) {
                String key = pair.split("=", 2)[0];
                if (!"token".equals(key)) {
                    filtered.add(pair);
                }
            }
            if (!filtered.isEmpty()) {
                sb.append("?").append(String.join("&", filtered));
            }
        }
        return sb.toString();
    }
}
