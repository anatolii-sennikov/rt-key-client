package io.github.anatoliisennikov.rtkey.internal.identity;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.CamelCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.codec.Json;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.CodeDispatch;

import java.util.UUID;

public final class SendCodeOperation {
    private static final String PATH = "/identity/api/v1/authorization/send_code";

    private SendCodeOperation() {
    }

    public static CodeDispatch call(HttpTransport transport, UUID deviceId, String phoneNumber) {
        String payload = Json.IDENTITY_MAPPER.writeValueAsString(new SendCodeRequest(phoneNumber));
        Response response = transport.post(Host.KEYAPIS, PATH, Body.identity(payload), deviceId);
        SendCodeData result = CamelCaseDecoder.data(response, SendCodeData.class);

        return new CodeDispatch(result.codeId(), result.timeout());
    }
}
