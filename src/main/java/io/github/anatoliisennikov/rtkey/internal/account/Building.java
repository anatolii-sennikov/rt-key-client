package io.github.anatoliisennikov.rtkey.internal.account;


import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

record Building(
        Integer id,
        Integer mrfId,
        Integer rfId,
        String place,
        String street,
        @Nullable
        String numbering,
        @Nullable
        Integer fiasId,
        Integer orponId,
        Integer companyId,
        Integer utcOffset,
        String kladrId,
        List<Entrance> entrances
) {
    Building {
        Objects.requireNonNull(entrances, "entrances is required");
    }
}
