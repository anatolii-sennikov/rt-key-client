package io.github.anatoliisennikov.rtkey.model;


import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

/**
 * An access key as returned by
 * {@link io.github.anatoliisennikov.rtkey.KeysService#createKey(int, String, String)} and
 * {@link io.github.anatoliisennikov.rtkey.KeysService#renameKey(int, String)}.
 *
 * @param id          key identifier
 * @param name        key name
 * @param code        key code
 * @param startDate   start date
 * @param endDate     end date, when set
 * @param flatId      flat identifier
 * @param createdBy   reported by the server; passed through without interpretation
 * @param description description, when set
 * @param devices     statuses of the key on the devices, when set
 */
public record Key(
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
        List<DeviceStatus> devices
) {
}
