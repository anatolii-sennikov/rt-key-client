package io.github.anatoliisennikov.rtkey.internal.events;

import java.util.Objects;

record AmountData(Integer total) {
    AmountData {
        Objects.requireNonNull(total, "total is required");
    }
}
