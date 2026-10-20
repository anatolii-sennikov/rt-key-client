package io.github.anatoliisennikov.rtkey.internal.account;


import java.util.List;
import java.util.Objects;

record Floor(
        String id,
        String name,
        List<Flat> flats
) {
    Floor {
        Objects.requireNonNull(flats, "flats is required");
    }
}
