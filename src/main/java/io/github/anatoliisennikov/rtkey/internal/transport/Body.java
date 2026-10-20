package io.github.anatoliisennikov.rtkey.internal.transport;

import java.util.Objects;


import static io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport.IDENTITY;
import static io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport.JSON;

public record Body(String contentType, String payload) {
    public Body {
        Objects.requireNonNull(contentType, "contentType");
        Objects.requireNonNull(payload, "payload");
    }

    public static Body json(String payload) {
        return new Body(JSON, payload);
    }

    public static Body identity(String payload) {
        return new Body(IDENTITY, payload);
    }

    boolean identity() {
        return IDENTITY.equals(contentType);
    }

    @Override
    public String toString() {
        return "Body[" + contentType + ", " + payload.length() + " chars]";
    }
}
