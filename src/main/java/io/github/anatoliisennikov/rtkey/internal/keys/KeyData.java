package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.model.DeviceStatus;
import io.github.anatoliisennikov.rtkey.model.Key;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

record KeyData(
        Integer id,
        String name,
        String code,
        Instant startDate,
        @Nullable
        Instant endDate,
        Integer flatId,
        String createdBy,
        @Nullable
        String description,
        @Nullable
        List<KeyDevice> devices
) {
    record KeyDevice(
            String deviceId,
            String deviceType,
            String status,
            String title
    ) {
    }

    static Key toModel(KeyData data) {
        return new Key(
                data.id,
                data.name,
                data.code,
                data.startDate,
                data.endDate,
                data.flatId,
                data.createdBy,
                data.description,
                data.devices == null ? null : data.devices.stream().map(
                                k -> new DeviceStatus(k.deviceId, k.deviceType, k.status, k.title))
                        .toList()
        );
    }
}
