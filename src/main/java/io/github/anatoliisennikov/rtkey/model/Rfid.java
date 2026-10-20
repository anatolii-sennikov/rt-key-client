package io.github.anatoliisennikov.rtkey.model;

import java.time.Instant;
import java.util.List;

/**
 * An RFID key as returned by {@link io.github.anatoliisennikov.rtkey.KeysService#rfids()}.
 *
 * @param id             RFID identifier
 * @param title          title
 * @param key            RFID value reported by the server
 * @param flatIds        flat identifiers
 * @param deviceIds      device identifiers
 * @param ownerType      owner type reported by the server; a server-defined code, not mapped
 *                       to an enum
 * @param payType        pay type reported by the server; a server-defined code, not mapped
 *                       to an enum
 * @param deviceStatuses statuses of the RFID on the devices
 * @param buildingIds    building identifiers
 * @param createdAt      creation timestamp
 * @param updatedAt      last update timestamp
 */
public record Rfid(
        Integer id,
        String title,
        String key,
        List<Integer> flatIds,
        List<Integer> deviceIds,
        String ownerType,
        String payType,
        List<DeviceStatus> deviceStatuses,
        List<Integer> buildingIds,
        Instant createdAt,
        Instant updatedAt
) {
    /**
     * Status of an RFID on a single device.
     *
     * @param deviceId  device identifier
     * @param rfidId    RFID identifier
     * @param status    status reported by the server; a server-defined code, not mapped to an enum
     * @param updatedAt time the status was updated
     */
    public record DeviceStatus(
            Integer deviceId,
            Integer rfidId,
            String status,
            Instant updatedAt
    ) {
    }
}
