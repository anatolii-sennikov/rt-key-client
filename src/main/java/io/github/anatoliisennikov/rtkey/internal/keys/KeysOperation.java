package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.Json;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Key;

import java.util.function.Supplier;

final class KeysOperation {
    private static final String PATH = "/api/v2/app/keys";

    private KeysOperation() {

    }

    public static Key createKey(HttpTransport transport, Supplier<String> tokenSupplier,
                                Integer flatId, String rfidUid, String name) {
        String payload = Json.JSON_MAPPER.writeValueAsString(new CreateKeyRequest(flatId, rfidUid, name));
        Response response = transport.post(Host.HOUSEHOLD, PATH, Body.json(payload), tokenSupplier);
        KeyData keyData = SnakeCaseDecoder.dataNode(response, "key", KeyData.class);
        return KeyData.toModel(keyData);
    }

    public static Key changeKeyName(HttpTransport transport, Supplier<String> tokenSupplier,
                                    Integer keyId, String name) {
        String path = PATH + "/" + keyId;

        String payload = Json.JSON_MAPPER.writeValueAsString(new RenameKeyRequest(name));
        Response response = transport.patch(Host.HOUSEHOLD, path, Body.json(payload), tokenSupplier);
        KeyData keyData = SnakeCaseDecoder.dataNode(response, "key", KeyData.class);
        return KeyData.toModel(keyData);
    }
}
