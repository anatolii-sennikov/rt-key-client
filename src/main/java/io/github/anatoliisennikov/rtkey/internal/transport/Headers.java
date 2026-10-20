package io.github.anatoliisennikov.rtkey.internal.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public final class Headers {
    private static final String AUTHORIZATION = "Authorization";
    private static final String DEVICE_ID = "X-Device-Id";
    private static final Headers EMPTY = new Headers(List.of());

    private final List<Entry> entries;

    private Headers(List<Entry> entries) {
        this.entries = entries;
    }

    public static Headers empty() {
        return EMPTY;
    }

    public static Headers of(String name, String value) {
        return empty().add(name, value);
    }

    public static Headers bearer(Supplier<String> tokenSupplier) {
        String token = tokenSupplier.get();
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("tokenSupplier returned null/blank token");
        }
        return empty().add(AUTHORIZATION, "Bearer " + token);
    }

    public static Headers deviceId(UUID deviceId) {
        return empty().add(DEVICE_ID, deviceId.toString());
    }

    public Headers add(String name, String value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");
        List<Entry> next = new ArrayList<>(entries);
        next.add(new Entry(name, value));
        return new Headers(List.copyOf(next));
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public List<Entry> entries() {
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Headers other)) {
            return false;
        }
        return entries.equals(other.entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    @Override
    public String toString() {
        return "Headers[" + entries.size() + " headers]";
    }

    public record Entry(String name, String value) {
    }
}
