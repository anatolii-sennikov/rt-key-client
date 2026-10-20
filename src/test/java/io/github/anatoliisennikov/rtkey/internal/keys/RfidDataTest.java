package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.model.Rfid;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RfidDataTest {
    private static final Instant CREATED_AT = Instant.parse("2024-11-01T09:00:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2025-03-15T12:30:45Z");
    private static final Instant STATUS_UPDATED_AT = Instant.parse("2025-04-02T07:05:10Z");

    @Test
    public void toModelMapsEveryField() {
        Rfid rfid = RfidData.toModel(rfidData());

        assertThat(rfid).isEqualTo(new Rfid(
                77001,
                "RFID-метка",
                "0011223344AA",
                List.of(301, 302),
                List.of(401, 402),
                "tenant",
                "prepaid",
                List.of(
                        new Rfid.DeviceStatus(11, 77001, "active", STATUS_UPDATED_AT),
                        new Rfid.DeviceStatus(12, 77001, "blocked", STATUS_UPDATED_AT)
                ),
                List.of(501),
                CREATED_AT,
                UPDATED_AT
        ));
    }

    @Test
    public void toModelRenamesRoomIdsToFlatIds() {
        Rfid rfid = RfidData.toModel(rfidData());

        assertThat(rfid.flatIds()).containsExactly(301, 302);
        assertThat(rfid.deviceIds()).containsExactly(401, 402);
        assertThat(rfid.buildingIds()).containsExactly(501);
    }

    @Test
    public void toModelRebuildsEveryDeviceStatus() {
        Rfid rfid = RfidData.toModel(rfidData());

        assertThat(rfid.deviceStatuses()).containsExactly(
                new Rfid.DeviceStatus(11, 77001, "active", STATUS_UPDATED_AT),
                new Rfid.DeviceStatus(12, 77001, "blocked", STATUS_UPDATED_AT)
        );
    }

    @Test
    public void toModelKeepsEmptyCollectionsEmpty() {
        RfidData data = new RfidData(
                77001,
                "RFID-метка",
                "0011223344AA",
                List.of(),
                List.of(),
                "tenant",
                "prepaid",
                List.of(),
                List.of(),
                CREATED_AT,
                UPDATED_AT
        );

        Rfid rfid = RfidData.toModel(data);

        assertThat(rfid.flatIds()).isEmpty();
        assertThat(rfid.deviceIds()).isEmpty();
        assertThat(rfid.deviceStatuses()).isEmpty();
        assertThat(rfid.buildingIds()).isEmpty();
    }

    private static RfidData rfidData() {
        return new RfidData(
                77001,
                "RFID-метка",
                "0011223344AA",
                List.of(301, 302),
                List.of(401, 402),
                "tenant",
                "prepaid",
                List.of(
                        new RfidData.DeviceStatus(11, 77001, "active", STATUS_UPDATED_AT),
                        new RfidData.DeviceStatus(12, 77001, "blocked", STATUS_UPDATED_AT)
                ),
                List.of(501),
                CREATED_AT,
                UPDATED_AT
        );
    }
}
