package io.github.anatoliisennikov.rtkey.internal.account;


import java.util.List;
import java.util.Objects;

record Entrance(
        String id,
        String name,
        List<Floor> floors
) {
    Entrance {
        Objects.requireNonNull(floors, "floors is required");
    }
}
