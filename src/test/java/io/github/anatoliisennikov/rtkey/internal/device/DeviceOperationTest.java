package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Device;
import io.github.anatoliisennikov.rtkey.model.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class DeviceOperationTest extends BaseOperationTest {

    @ParameterizedTest
    @MethodSource("deviceIds")
    public void devices(Integer ids) throws IOException {
        final String data = getResourceAsString("/device/device.json");
        Query query = ids == null ? Query.empty() : Query.of("ids", String.valueOf(ids));

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v4/devices"), eq(query), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Page<Device> page = DeviceOperation.call(transport, tokenSupplier, ids);

        Device expectedDevice = expectedDevice();

        Page<Device> expected = new Page<>(List.of(expectedDevice), 1);
        assertThat(page).isEqualTo(expected);
    }

    @Test
    public void devicesWithUnknownIds() throws IOException {
        final String data = getResourceAsString("/device/empty_devices.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v4/devices"), eq(Query.of("ids", "42")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Page<Device> page = DeviceOperation.call(transport, tokenSupplier, 42);

        Page<Device> expected = new Page<>(List.of(), 0);
        assertThat(page).isEqualTo(expected);
    }

    private static Device expectedDevice() {
        return new Device(
                11155,
                4567,
                "intercom",
                "qtech",
                "подъезд №1",
                "online",
                "sync_success",
                Instant.parse("2025-11-05T18:06:12Z"),
                null,
                0,
                UUID.fromString("efe45ee2-8c3b-41a0-8b22-e2702db6087e"),
                "processing_error",
                "URL имеет неверное значение"
        );
    }

    private static Stream<Arguments> deviceIds() {
        return Stream.of(
                Arguments.of((Integer) null),
                Arguments.of(42)
        );
    }
}
