package io.github.anatoliisennikov.rtkey.internal.code;

import java.time.Instant;
import java.util.Objects;

record CodeStatus(
        StatusesDevice device,
        Instant statusChangedAt,
        String status
) {
    CodeStatus {
        Objects.requireNonNull(device, "device is required");
    }
}
