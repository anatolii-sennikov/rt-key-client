package io.github.anatoliisennikov.rtkey.model;

import java.time.Instant;

/**
 * An entry of the per-device short feed, returned by
 * {@link io.github.anatoliisennikov.rtkey.EventService#deviceEvent(int)}.
 *
 * <p>Unlike the raw events journal, this feed carries the event type as a number
 * plus the event name as text.
 *
 * @param deviceId   device identifier
 * @param deviceType device type reported by the server
 * @param userId     user identifier
 * @param timestamp  event timestamp
 * @param eventType  event type number reported by the server; a server-defined code, not mapped
 *                   to an enum
 * @param eventName  event name reported by the server
 * @param code       code reported by the server; may be an empty string
 * @param flatNumber flat number
 */
public record DeviceEvent(
        Integer deviceId,
        String deviceType,
        Integer userId,
        Instant timestamp,
        Integer eventType,
        String eventName,
        String code,
        String flatNumber
) {
}
