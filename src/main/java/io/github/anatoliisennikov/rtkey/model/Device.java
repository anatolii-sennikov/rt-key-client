package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

/**
 * A device card as returned by {@link io.github.anatoliisennikov.rtkey.DeviceService#devices()}.
 *
 * @param id             device identifier
 * @param companyId      identifier of the company the device belongs to
 * @param deviceType     device type reported by the server
 * @param modelType      device model reported by the server
 * @param title          device title
 * @param status         device status reported by the server
 * @param syncStatus     synchronization status reported by the server
 * @param syncTime       time of the last synchronization reported by the server
 * @param syncError      synchronization error reported by the server, when set
 * @param syncRetryCount number of synchronization retries reported by the server
 * @param cameraUuid     UID of the camera associated with the device
 * @param cameraStatus   camera status reported by the server
 * @param cameraError    camera error reported by the server, when set
 */
public record Device(
        Integer id,
        Integer companyId,
        String deviceType,
        String modelType,
        String title,
        String status,
        String syncStatus,
        Instant syncTime,
        @Nullable
        String syncError,
        Integer syncRetryCount,
        UUID cameraUuid,
        String cameraStatus,
        @Nullable
        String cameraError
) {
}
