package io.github.anatoliisennikov.rtkey.internal.identity;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.CamelCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.codec.Json;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Session;

import java.util.UUID;

public final class LoginOperation {
    private static final String PATH = "/identity/api/v1/authorization/login";

    private LoginOperation() {
    }

    public static Session call(HttpTransport transport, UUID deviceId, String code, String codeId) {
        String payload = Json.IDENTITY_MAPPER.writeValueAsString(new LoginRequest(code, codeId));
        Response response = transport.post(Host.KEYAPIS, PATH, Body.identity(payload), deviceId);
        LoginData result = CamelCaseDecoder.data(response, LoginData.class);

        return new Session(result.accessToken(), result.expiredAt());
    }
}
