package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.Json;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Intercom;

import java.util.function.Supplier;

final class RenameOperation {
    private static final String PATH = "/api/v2/app/devices/intercom/{id}";

    private RenameOperation() {

    }

    public static Intercom call(HttpTransport transport, Supplier<String> tokenSupplier, String id, String name) {
        String path = PATH.replace("{id}", id);
        String payload = Json.JSON_MAPPER.writeValueAsString(new RenameRequest(name));
        Response response = transport.patch(Host.HOUSEHOLD,
                path,
                Body.json(payload),
                tokenSupplier);
        IntercomData intercomData = SnakeCaseDecoder.dataNode(response, "device", IntercomData.class);

        return IntercomData.toModel(intercomData);
    }

}
