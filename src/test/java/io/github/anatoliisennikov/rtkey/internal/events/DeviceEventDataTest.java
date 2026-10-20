package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.model.DeviceEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceEventDataTest {
    private static final Instant TIMESTAMP = Instant.parse("2025-06-11T13:14:15Z");

    @Test
    public void toModelMapsEveryField() {
        DeviceEventData data = new DeviceEventData(
                101,
                "intercom",
                404,
                TIMESTAMP,
                17,
                "Открытие двери",
                "1234",
                "77"
        );

        DeviceEvent event = DeviceEventData.toModel(data);

        assertThat(event).isEqualTo(new DeviceEvent(
                101,
                "intercom",
                404,
                TIMESTAMP,
                17,
                "Открытие двери",
                "1234",
                "77"
        ));
    }

    @Test
    public void toModelKeepsNumericFieldsApart() {
        DeviceEventData data = new DeviceEventData(
                101,
                "intercom",
                404,
                TIMESTAMP,
                17,
                "Открытие двери",
                "1234",
                "77"
        );

        DeviceEvent event = DeviceEventData.toModel(data);

        assertThat(event.deviceId()).isEqualTo(101);
        assertThat(event.userId()).isEqualTo(404);
        assertThat(event.eventType()).isEqualTo(17);
    }
}
