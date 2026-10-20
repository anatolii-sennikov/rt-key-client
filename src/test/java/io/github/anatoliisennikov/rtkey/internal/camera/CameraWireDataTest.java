package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.model.CameraData;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CameraWireDataTest {
    private static final UUID CAMERA_UID = UUID.fromString("777bcaa6-2222-4444-8888-5b92bc6808e5");
    private static final Instant CREATED_AT = Instant.parse("2023-02-13T14:19:48Z");
    private static final Instant UPDATED_AT = Instant.parse("2025-01-26T11:44:13Z");
    private static final String SCREENSHOT_URL_TEMPLATE =
            "https://media.example/image/{size}/{timestamp}.jpg?token={cdn_token}";
    private static final String SCREENSHOT_PRECISE_URL_TEMPLATE =
            "https://media.example/image/precise/{size}/{timestamp}.jpg?token={cdn_token}";

    @Test
    public void toModelMapsEveryPublicField() {
        CameraData camera = CameraWireData.toModel(cameraWireData(status(), location()));

        assertThat(camera).isEqualTo(new CameraData(
                CAMERA_UID,
                "Джеймс05/007",
                "https://live.example/blue7",
                "online",
                "Доступна",
                SCREENSHOT_URL_TEMPLATE,
                SCREENSHOT_PRECISE_URL_TEMPLATE,
                "user.token",
                "streamer.token",
                "screenshot.token",
                10080,
                new CameraData.Location(55.751244, 37.618492),
                CREATED_AT,
                UPDATED_AT,
                180
        ));
    }

    @Test
    public void toModelFlattensStatusIntoTypeAndTitle() {
        CameraData camera = CameraWireData.toModel(cameraWireData(status(), location()));

        assertThat(camera.statusType()).isEqualTo("online");
        assertThat(camera.statusTitle()).isEqualTo("Доступна");
    }

    @Test
    public void toModelRebuildsLocationAsModelRecord() {
        CameraData camera = CameraWireData.toModel(cameraWireData(status(), location()));

        CameraData.Location location =
                Objects.requireNonNull(camera.location(), "a present location must be mapped");

        assertThat(location).isEqualTo(new CameraData.Location(55.751244, 37.618492));
        assertThat(location.lat()).isEqualTo(55.751244);
        assertThat(location.lng()).isEqualTo(37.618492);
    }

    @Test
    public void toModelKeepsAbsentStatusNull() {
        CameraData camera = CameraWireData.toModel(cameraWireData(null, location()));

        assertThat(camera.statusType()).isNull();
        assertThat(camera.statusTitle()).isNull();
        assertThat(camera.location()).isEqualTo(new CameraData.Location(55.751244, 37.618492));
    }

    @Test
    public void toModelKeepsAbsentLocationNull() {
        CameraData camera = CameraWireData.toModel(cameraWireData(status(), null));

        assertThat(camera.location()).isNull();
        assertThat(camera.statusType()).isEqualTo("online");
        assertThat(camera.statusTitle()).isEqualTo("Доступна");
    }

    @Test
    public void toModelKeepsBothAbsentStatusAndLocationNull() {
        CameraData camera = CameraWireData.toModel(cameraWireData(null, null));

        assertThat(camera.statusType()).isNull();
        assertThat(camera.statusTitle()).isNull();
        assertThat(camera.location()).isNull();
        assertThat(camera.uid()).isEqualTo(CAMERA_UID);
    }

    @Test
    public void toModelIgnoresWireOnlyFields() {
        CameraData withRecordedWireFields = CameraWireData.toModel(cameraWireData(status(), location()));
        CameraData withOtherWireFields = CameraWireData.toModel(new CameraWireData(
                CAMERA_UID,
                new CameraWireData.Category(99, "yard", "Дворовая"),
                "Джеймс05/007",
                "SN-9999",
                "10.9.9.9",
                "https://live.example/blue7",
                "11:22:33:44:55:66",
                "Другая модель",
                "Другой вендор",
                status(),
                SCREENSHOT_URL_TEMPLATE,
                SCREENSHOT_PRECISE_URL_TEMPLATE,
                "user.token",
                "streamer.token",
                "screenshot.token",
                10080,
                location(),
                CREATED_AT,
                UPDATED_AT,
                180
        ));

        assertThat(withRecordedWireFields).isEqualTo(withOtherWireFields);
    }

    private static CameraWireData.Status status() {
        return new CameraWireData.Status(2, "online", "Доступна");
    }

    private static CameraWireData.Location location() {
        return new CameraWireData.Location(55.751244, 37.618492);
    }

    private static CameraWireData cameraWireData(CameraWireData.@Nullable Status status,
                                                 CameraWireData.@Nullable Location location) {
        return new CameraWireData(
                CAMERA_UID,
                new CameraWireData.Category(1, "entrance", "Входная"),
                "Джеймс05/007",
                "SN-0001",
                "10.0.0.7",
                "https://live.example/blue7",
                "AA:BB:CC:DD:EE:FF",
                "Sputnik-3",
                "RT",
                status,
                SCREENSHOT_URL_TEMPLATE,
                SCREENSHOT_PRECISE_URL_TEMPLATE,
                "user.token",
                "streamer.token",
                "screenshot.token",
                10080,
                location,
                CREATED_AT,
                UPDATED_AT,
                180
        );
    }
}
