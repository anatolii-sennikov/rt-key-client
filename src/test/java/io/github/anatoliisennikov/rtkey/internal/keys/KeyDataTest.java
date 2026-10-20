package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.model.DeviceStatus;
import io.github.anatoliisennikov.rtkey.model.Key;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyDataTest {
    private static final Instant START_DATE = Instant.parse("2025-01-10T08:15:30Z");
    private static final Instant END_DATE = Instant.parse("2025-02-20T18:45:00Z");

    @Test
    public void toModelMapsEveryScalarField() {
        Key key = KeyData.toModel(keyData(END_DATE, "Ключ курьера", devices()));

        assertThat(key).isEqualTo(new Key(
                55552501,
                "Домофонный ключ",
                "4457262AEB6D01",
                START_DATE,
                END_DATE,
                55554411,
                "tenant",
                "Ключ курьера",
                List.of(
                        new DeviceStatus("dev-1", "intercom", "active", "Подъезд 1"),
                        new DeviceStatus("dev-2", "barrier", "inactive", "Шлагбаум 2")
                )
        ));
    }

    @Test
    public void toModelMapsEveryDeviceFieldInOrder() {
        Key key = KeyData.toModel(keyData(null, null, devices()));

        assertThat(key.devices()).containsExactly(
                new DeviceStatus("dev-1", "intercom", "active", "Подъезд 1"),
                new DeviceStatus("dev-2", "barrier", "inactive", "Шлагбаум 2")
        );
    }

    @Test
    public void toModelKeepsAbsentDevicesNull() {
        Key key = KeyData.toModel(keyData(END_DATE, "Ключ курьера", null));

        assertThat(key.devices()).isNull();
    }

    @Test
    public void toModelKeepsEmptyDevicesEmpty() {
        Key key = KeyData.toModel(keyData(END_DATE, "Ключ курьера", List.of()));

        assertThat(key.devices()).isEmpty();
    }

    @Test
    public void toModelKeepsNullableScalarsAbsent() {
        Key key = KeyData.toModel(keyData(null, null, devices()));

        assertThat(key.endDate()).isNull();
        assertThat(key.description()).isNull();
    }

    private static List<KeyData.KeyDevice> devices() {
        return List.of(
                new KeyData.KeyDevice("dev-1", "intercom", "active", "Подъезд 1"),
                new KeyData.KeyDevice("dev-2", "barrier", "inactive", "Шлагбаум 2")
        );
    }

    private static KeyData keyData(@Nullable Instant endDate, @Nullable String description,
                                   @Nullable List<KeyData.KeyDevice> devices) {
        return new KeyData(
                55552501,
                "Домофонный ключ",
                "4457262AEB6D01",
                START_DATE,
                endDate,
                55554411,
                "tenant",
                description,
                devices
        );
    }
}
