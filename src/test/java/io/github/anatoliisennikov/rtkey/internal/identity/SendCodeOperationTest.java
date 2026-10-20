package io.github.anatoliisennikov.rtkey.internal.identity;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.CodeDispatch;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendCodeOperationTest extends BaseOperationTest {
    private static final String PATH = "/identity/api/v1/authorization/send_code";

    private final ArgumentCaptor<Body> body = ArgumentCaptor.forClass(Body.class);

    @Test
    public void sendCode() {
        String data = "{\"data\":{\"codeId\":\"<codeId>\",\"timeout\":120}}";

        UUID deviceId = UUID.randomUUID();
        when(transport.post(eq(Host.KEYAPIS), eq(PATH), any(Body.class), eq(deviceId)))
                .thenReturn(new Response(200, data));

        CodeDispatch codeDispatch = SendCodeOperation.call(transport, deviceId, "+71234567");

        verify(transport).post(eq(Host.KEYAPIS), eq(PATH), body.capture(), eq(deviceId));
        assertIdentityBody(body.getValue(), "{\"phoneNumber\":\"+71234567\"}");

        CodeDispatch expectedCodeDispatch = new CodeDispatch("<codeId>", 120);
        assertThat(codeDispatch).isEqualTo(expectedCodeDispatch);
    }

    @Test
    public void sendCodeInvalidPhoneNumber() {
        String data = "{\"code\":3, \"message\":\"\", \"details\":[]}";

        UUID deviceId = UUID.randomUUID();
        when(transport.post(eq(Host.KEYAPIS), eq(PATH), any(Body.class), eq(deviceId)))
                .thenReturn(new Response(400, data));

        assertThatThrownBy(() -> SendCodeOperation.call(transport, deviceId, "+767"))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("API error 3 (HTTP 400)");

        verify(transport).post(eq(Host.KEYAPIS), eq(PATH), body.capture(), eq(deviceId));
        assertIdentityBody(body.getValue(), "{\"phoneNumber\":\"+767\"}");
    }
}
