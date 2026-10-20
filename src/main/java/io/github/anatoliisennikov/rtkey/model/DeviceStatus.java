package io.github.anatoliisennikov.rtkey.model;

/**
 * Status of a code or key on a single device.
 *
 * @param deviceId   device identifier
 * @param deviceType device type reported by the server; a server-defined code, not mapped
 *                   to an enum
 * @param status     status reported by the server; a server-defined code, not mapped to an enum
 * @param title      device title
 */
public record DeviceStatus(
        String deviceId,
        String deviceType,
        String status,
        String title
) {
}
