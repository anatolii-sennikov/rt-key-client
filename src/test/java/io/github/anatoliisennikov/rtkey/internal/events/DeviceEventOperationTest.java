package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.DeviceEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class DeviceEventOperationTest extends BaseOperationTest {

    @Test
    public void deviceEvents() throws IOException {
        final String data = getResourceAsString("/events/devices_events.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v1/app/devices/777/events"), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<DeviceEvent> deviceEvents = DeviceEventOperation.call(transport, tokenSupplier, 777);

        List<DeviceEvent> expected = List.of(
                new DeviceEvent(
                        11133,
                        "intercom",
                        1771999,
                        Instant.parse("2025-11-10T12:05:27Z"),
                        11,
                        "Открыта основная дверь с помощью удаленного доступа",
                        "",
                        "11"
                ),
                new DeviceEvent(
                        11133,
                        "intercom",
                        1771999,
                        Instant.parse("2025-11-10T11:29:13Z"),
                        11,
                        "Открыта основная дверь с помощью удаленного доступа",
                        "",
                        "11"
                ),
                new DeviceEvent(
                        11133,
                        "intercom",
                        1771999,
                        Instant.parse("2025-11-10T09:44:16Z"),
                        11,
                        "Открыта основная дверь с помощью удаленного доступа",
                        "",
                        "11"
                )
        );

        assertThat(deviceEvents).isEqualTo(expected);
    }

    @Test
    public void eventsDeviceNotFound() throws IOException {
        final String data = getResourceAsString("/v2/devices_not_found.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v1/app/devices/288/events"), same(tokenSupplier)))
                .thenReturn(new Response(404, data));

        assertThatThrownBy(() -> DeviceEventOperation.call(transport, tokenSupplier, 288))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Устройство не найдено: device not found, id: 288");
    }
}

