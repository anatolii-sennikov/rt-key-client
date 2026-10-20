package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.model.DeviceStatus;
import io.github.anatoliisennikov.rtkey.model.Intercode;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

record IntercodeData(
        Integer id,
        String code,
        Instant startDate,
        Instant endDate,
        String interCodeType,
        Boolean common,
        List<Status> statuses


) {
    IntercodeData {
        Objects.requireNonNull(statuses, "statuses is required");
    }

    record Status(
            String deviceId,
            String deviceType,
            String status,
            String title
    ) {
    }

    static Intercode toModel(IntercodeData data) {
        return new Intercode(
                data.id(),
                data.code(),
                data.startDate(),
                data.endDate(),
                data.interCodeType(),
                data.common(),
                data.statuses().stream().map(status -> new DeviceStatus(
                        status.deviceId(),
                        status.deviceType(),
                        status.status(),
                        status.title()
                )).toList()
        );
    }
}
