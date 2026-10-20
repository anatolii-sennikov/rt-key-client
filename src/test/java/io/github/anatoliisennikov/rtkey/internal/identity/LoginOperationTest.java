package io.github.anatoliisennikov.rtkey.internal.identity;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Session;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginOperationTest extends BaseOperationTest {
    private static final String PATH = "/identity/api/v1/authorization/login";

    private final ArgumentCaptor<Body> body = ArgumentCaptor.forClass(Body.class);

    @Test
    public void login() throws IOException {
        final String data = getResourceAsString("/identity/login.json");

        UUID deviceId = UUID.randomUUID();
        when(transport.post(eq(Host.KEYAPIS), eq(PATH), any(Body.class), eq(deviceId)))
                .thenReturn(new Response(200, data));

        Session session = LoginOperation.call(transport, deviceId, "12345", "somecodeid");

        verify(transport).post(eq(Host.KEYAPIS), eq(PATH), body.capture(), eq(deviceId));
        assertIdentityBody(body.getValue(), "{\"code\":\"12345\",\"codeId\":\"somecodeid\"}");

        Session expectedSession = new Session("jwt", Instant.parse("2025-03-06T07:08:24Z"));
        assertThat(session).isEqualTo(expectedSession);
    }

    @Test
    public void loginInvalidDeviceId() throws IOException {
        final String data = getResourceAsString("/identity/invalid_device_id.json");

        UUID deviceId = UUID.randomUUID();
        when(transport.post(eq(Host.KEYAPIS), eq(PATH), any(Body.class), eq(deviceId)))
                .thenReturn(new Response(400, data));

        assertThatThrownBy(() -> LoginOperation.call(transport, deviceId, "12345", "somecodeid"))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("1025:Invalid device_id");

        verify(transport).post(eq(Host.KEYAPIS), eq(PATH), body.capture(), eq(deviceId));
        assertIdentityBody(body.getValue(), "{\"code\":\"12345\",\"codeId\":\"somecodeid\"}");
    }
}
