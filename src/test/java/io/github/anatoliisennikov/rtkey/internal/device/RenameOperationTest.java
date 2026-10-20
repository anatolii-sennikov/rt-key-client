package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Intercom;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RenameOperationTest extends BaseOperationTest {
    private final ArgumentCaptor<Body> body = ArgumentCaptor.forClass(Body.class);

    @Test
    public void rename() throws IOException {
        final String data = getResourceAsString("/device/rename_device.json");
        final String path = "/api/v2/app/devices/intercom/123456";

        when(transport.patch(eq(Host.HOUSEHOLD), eq(path), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Intercom intercom = RenameOperation.call(transport, tokenSupplier, "123456", "new name");

        verify(transport).patch(eq(Host.HOUSEHOLD), eq(path), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), "{\"name_by_user\":\"new name\"}");

        Intercom expected = new Intercom(
                "99911",
                "1c555550qwe2",
                List.of("Калитка"),
                "27d056d3-c00f-42c6-8a42-ad0d3905117a",
                "подъезд №1",
                true,
                "подъезд №1",
                "new name"
        );
        assertThat(intercom).isEqualTo(expected);
    }

    @Test
    public void renameDeviceNotFound() throws IOException {
        final String data = getResourceAsString("/v2/devices_not_found.json");
        final String path = "/api/v2/app/devices/intercom/288";

        when(transport.patch(eq(Host.HOUSEHOLD), eq(path), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(404, data));

        assertThatThrownBy(() -> RenameOperation.call(transport, tokenSupplier, "288", "new name"))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Устройство не найдено: device not found, id: 288");

        verify(transport).patch(eq(Host.HOUSEHOLD), eq(path), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), "{\"name_by_user\":\"new name\"}");
    }
}
