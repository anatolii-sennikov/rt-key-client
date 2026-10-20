package io.github.anatoliisennikov.rtkey.internal.transport;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Query {
    private static final Query EMPTY = new Query(List.of());

    private final List<String> pairs;

    private Query(List<String> pairs) {
        this.pairs = pairs;
    }

    public static Query empty() {
        return EMPTY;
    }

    public static Query of(String key, String value) {
        return empty().add(key, value);
    }

    public Query add(String key, String value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        List<String> next = new ArrayList<>(pairs);
        next.add(encode(key) + '=' + encode(value));
        return new Query(List.copyOf(next));
    }

    public boolean isEmpty() {
        return pairs.isEmpty();
    }

    public String encoded() {
        return String.join("&", pairs);
    }

    @Override
    public String toString() {
        return "Query[" + pairs.size() + " params]";
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Query other)) return false;
        return pairs.equals(other.pairs);
    }

    @Override
    public int hashCode() {
        return pairs.hashCode();
    }
}
