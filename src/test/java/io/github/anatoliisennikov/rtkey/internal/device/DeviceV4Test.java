package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.model.Device;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceV4Test {
    private static final UUID CAMERA_UID = UUID.fromString("777bcaa6-2222-4444-8888-5b92bc6808e5");
    private static final Instant SYNC_AT = Instant.parse("2025-06-01T10:00:00Z");
    private static final Instant SUCCESSFUL_SYNC_AT = Instant.parse("2025-05-31T09:00:00Z");
    private static final Instant EMERGENCY_DOOR_STATUS_AT = Instant.parse("2025-05-30T08:00:00Z");

    @Test
    public void toModelMapsEveryPublicField() {
        Device device = DeviceV4.toModel(deviceV4("sync timeout", "camera timeout"));

        assertThat(device).isEqualTo(new Device(
                9001,
                4321,
                "intercom",
                "sputnik-3",
                "Подъезд 1",
                "active",
                "synced",
                SYNC_AT,
                "sync timeout",
                3,
                CAMERA_UID,
                "online",
                "camera timeout"
        ));
    }

    @Test
    public void toModelRenamesWireFieldsToModelNames() {
        Device device = DeviceV4.toModel(deviceV4("sync timeout", "camera timeout"));

        assertThat(device.status()).isEqualTo("active");
        assertThat(device.syncStatus()).isEqualTo("synced");
        assertThat(device.syncTime()).isEqualTo(SYNC_AT);
        assertThat(device.cameraUuid()).isEqualTo(CAMERA_UID);
    }

    @Test
    public void toModelKeepsTheTwoCameraFieldsApart() {
        Device device = DeviceV4.toModel(deviceV4("sync timeout", "camera timeout"));

        assertThat(device.cameraStatus()).isEqualTo("online");
        assertThat(device.cameraError()).isEqualTo("camera timeout");
        assertThat(device.status()).isEqualTo("active");
        assertThat(device.syncError()).isEqualTo("sync timeout");
    }

    @Test
    public void toModelKeepsNullableErrorFieldsAbsent() {
        Device device = DeviceV4.toModel(deviceV4(null, null));

        assertThat(device.syncError()).isNull();
        assertThat(device.cameraError()).isNull();
        assertThat(device.syncStatus()).isEqualTo("synced");
        assertThat(device.cameraStatus()).isEqualTo("online");
    }

    private static DeviceV4 deviceV4(@Nullable String syncError, @Nullable String cameraError) {
        return new DeviceV4(
                9001,
                4321,
                List.of(4321, 4322),
                List.of(10, 20),
                List.of(1),
                "intercom",
                "sputnik-3",
                "SN-0001",
                "AA:BB:CC:DD:EE:FF",
                "Подъезд 1",
                "10.0.0.7",
                "active",
                "1.2.3",
                "4.5.6",
                true,
                new DeviceSettings(
                        "autocollect",
                        "code",
                        "door",
                        "dtmf",
                        new EmergencyDoor("closed", "final", EMERGENCY_DOOR_STATUS_AT, "none", 0),
                        "gate",
                        "ntp",
                        "rfid",
                        "sip",
                        "syslog"
                ),
                "synced",
                SYNC_AT,
                syncError,
                3,
                SUCCESSFUL_SYNC_AT,
                false,
                List.of("tag-1"),
                List.of("tenant"),
                new TrunkAccount("74950000001", "sip.example", 5060, "outgoing"),
                new Redirect(true, false),
                true,
                "public-identity",
                CAMERA_UID,
                "online",
                cameraError
        );
    }
}
