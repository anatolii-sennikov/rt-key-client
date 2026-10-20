package io.github.anatoliisennikov.rtkey.internal.transport;

import java.util.Objects;

public record Response(int status, String body, String contentType, String request) {
    public Response {
        Objects.requireNonNull(body, "body");
        Objects.requireNonNull(contentType, "contentType");
        Objects.requireNonNull(request, "request");
    }

    public Response(int status, String body) {
        this(status, body, "", "");
    }

    public boolean success() {
        return status >= 200 && status < 300;
    }
}
