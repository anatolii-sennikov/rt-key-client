package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.model.CameraData;
import org.jspecify.annotations.Nullable;


import java.time.Instant;
import java.util.UUID;

record CameraWireData(
        UUID uid,
        Category category,
        String title,
        String serialNumber,
        String ip,
        String streamerUrl,
        String mac,
        String model,
        String vendor,
        @Nullable
        Status status,
        String screenshotUrlTemplate,
        String screenshotPreciseUrlTemplate,
        String userToken,
        String streamerToken,
        String screenshotToken,
        Integer archiveLength,
        @Nullable
        Location location,
        Instant createdAt,
        Instant updatedAt,
        Integer utcOffset
) {
    record Category(
            Integer id,
            String type,
            String title
    ) {
    }

    record Status(
            Integer id,
            String type,
            String title
    ) {
    }

    record Location(double lat, double lng) {}

    static CameraData toModel(CameraWireData data) {
        return new CameraData(
                data.uid(),
                data.title(),
                data.streamerUrl(),
                data.status() == null ? null : data.status().type(),
                data.status() == null ? null : data.status().title(),
                data.screenshotUrlTemplate(),
                data.screenshotPreciseUrlTemplate(),
                data.userToken(),
                data.streamerToken(),
                data.screenshotToken(),
                data.archiveLength(),
                data.location() == null
                        ? null
                        : new CameraData.Location(data.location().lat(), data.location().lng()),
                data.createdAt(),
                data.updatedAt(),
                data.utcOffset()
        );
    }
}
