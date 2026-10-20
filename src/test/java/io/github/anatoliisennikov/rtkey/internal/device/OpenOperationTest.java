package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OpenOperationTest extends BaseOperationTest {

    @Test
    public void open() {
        when(transport.post(eq(Host.HOUSEHOLD), eq("/api/v2/app/devices/123456/open"), same(tokenSupplier)))
                .thenReturn(new Response(200, ""));

        assertThatCode(() -> OpenOperation.call(transport, tokenSupplier, "123456"))
                .doesNotThrowAnyException();

        verify(transport).post(eq(Host.HOUSEHOLD), eq("/api/v2/app/devices/123456/open"), same(tokenSupplier));
    }

    @Test
    public void openNotFoundDevice() throws IOException {
        final String data = getResourceAsString("/device/device_not_found.json");

        when(transport.post(eq(Host.HOUSEHOLD), eq("/api/v2/app/devices/123456/open"), same(tokenSupplier)))
                .thenReturn(new Response(404, data));

        assertThatThrownBy(() -> OpenOperation.call(transport, tokenSupplier, "123456"))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Устройство не связано с квартирой: device has no connection with flat");
    }
}
