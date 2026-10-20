package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.model.Rfid;

import java.time.Instant;
import java.util.List;

record RfidData(
        Integer id,
        String title,
        String key,
        List<Integer> roomIds,
        List<Integer> deviceIds,
        String ownerType,
        String payType,
        List<DeviceStatus> deviceStatuses,
        List<Integer> buildingIds,
        Instant createdAt,
        Instant updatedAt
) {
    record DeviceStatus(
            Integer deviceId,
            Integer rfidId,
            String status,
            Instant updatedAt
    ) {
    }

    static Rfid toModel(RfidData data) {
       return new Rfid(
                data.id(),
                data.title(),
                data.key(),
                data.roomIds(),
                data.deviceIds(),
                data.ownerType(),
                data.payType(),
                data.deviceStatuses().stream().map(
                        ds -> new Rfid.DeviceStatus(
                                ds.deviceId(),
                                ds.rfidId(),
                                ds.status(),
                                ds.updatedAt()
                        )
                ).toList(),
                data.buildingIds(),
                data.createdAt(),
                data.updatedAt()
        );
    }
}
