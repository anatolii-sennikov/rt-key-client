package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.EmergencyCode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class EmergencyCodeOperationTest extends BaseOperationTest {

    @Test
    public void emergencyCodes() throws IOException {
        final String data = getResourceAsString("/code/emergency_code_by_deviceId.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v3/app/codes/device/77777/type/emergency"), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<EmergencyCode> emergencyCodes = EmergencyCodeOperation.call(transport, tokenSupplier, 77777);

        EmergencyCode expected = new EmergencyCode(
                22349991,
                "99014",
                "emergency",
                null,
                null,
                null,
                "system",
                null,
                "system",
                null,
                Instant.parse("2025-11-07T18:36:06Z"),
                Instant.parse("2025-11-07T18:36:06Z"),
                Instant.parse("2025-11-14T18:36:06Z"),
                List.of(new EmergencyCode.Status(
                        new EmergencyCode.Device(
                                65758,
                                new EmergencyCode.Company(3399),
                                "intercom",
                                "подъезд №4",
                                null,
                                null
                        ),
                        Instant.parse("2025-11-07T18:36:06Z"),
                        "loaded"
                ))
        );

        assertThat(emergencyCodes).isEqualTo(List.of(expected));
    }

    @Test
    public void emergencyNotExistedDeviceCodes() throws IOException {
        final String data = getResourceAsString("/code/emergency_code_by_wrong_deviceId.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v3/app/codes/device/77777/type/emergency"), same(tokenSupplier)))
                .thenReturn(new Response(404, data));

        assertThatThrownBy(() -> EmergencyCodeOperation.call(transport, tokenSupplier, 77777))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Объект не найден: device has no connection with flat");
    }
}
