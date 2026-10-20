package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.model.DeviceEvent;

import java.time.Instant;

record DeviceEventData(
        Integer deviceId,
        String deviceType,
        Integer userId,
        Instant timestamp,
        Integer eventType,
        String eventName,
        String code,
        String flatNumber
) {
    static DeviceEvent toModel(DeviceEventData data) {
        return new DeviceEvent(
                data.deviceId(),
                data.deviceType(),
                data.userId(),
                data.timestamp(),
                data.eventType(),
                data.eventName(),
                data.code(),
                data.flatNumber()
        );
    }
}
