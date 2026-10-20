package io.github.anatoliisennikov.rtkey.internal.codec;

import io.github.anatoliisennikov.rtkey.exception.RtKeyContractException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;

import java.util.Objects;

public final class EmptyDecoder {
    private EmptyDecoder() {
    }

    public static void decode(Response response) {
        Objects.requireNonNull(response, "response");
        if (!response.success()) {
            throw ServerErrors.of(response);
        }

        if (!response.body().isBlank()) {
            throw new RtKeyContractException(
                    "empty response carries a body (HTTP " + response.status() + ")");
        }
    }
}
