package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.model.Event;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

record EventData(
        Instant raisedAt,
        String eventType,
        @Nullable
        Integer deviceId,
        @Nullable
        String deviceType,
        @Nullable
        Integer companyId,
        @Nullable
        Integer roomId,
        @Nullable
        Integer relay,
        Integer userId,
        String userAgent,
        @Nullable
        Integer buildingId,
        @Nullable
        Long orponId,
        @Nullable
        String roomNumber,
        @Nullable
        String ivrPhoneNumber,
        @Nullable
        String guestPhoneNumber,
        @Nullable
        String code,
        @Nullable
        String rfid,
        String requestId,
        @Nullable
        String dtmfPhoneNumber,
        @Nullable
        String deviceUserType,
        @Nullable
        String login,
        @Nullable
        String password,
        @Nullable
        String host,
        @Nullable
        String deviceModel,
        @Nullable
        String os,
        @Nullable
        String pushToken,
        @Nullable
        Integer codeId,
        @Nullable
        Integer rfidId,
        @Nullable
        String cameraUid,
        @Nullable
        String screenshotUrl,
        @Nullable
        String carNumber,
        @Nullable
        String vehicle,
        @Nullable
        String courierCompanyName,
        @Nullable
        String orderNumber,
        @Nullable
        String courierId
) {
    static Event toModel(EventData data) {
        return new Event(
                data.raisedAt(),
                data.eventType(),
                data.deviceId(),
                data.deviceType(),
                data.companyId(),
                data.roomId(),
                data.relay(),
                data.userId(),
                data.userAgent(),
                data.buildingId(),
                data.orponId(),
                data.roomNumber(),
                data.ivrPhoneNumber(),
                data.guestPhoneNumber(),
                data.code(),
                data.rfid(),
                data.requestId(),
                data.dtmfPhoneNumber(),
                data.deviceUserType(),
                data.login(),
                data.password(),
                data.host(),
                data.deviceModel(),
                data.os(),
                data.pushToken(),
                data.codeId(),
                data.rfidId(),
                data.cameraUid(),
                data.screenshotUrl(),
                data.carNumber(),
                data.vehicle(),
                data.courierCompanyName(),
                data.orderNumber(),
                data.courierId()
        );
    }
}
