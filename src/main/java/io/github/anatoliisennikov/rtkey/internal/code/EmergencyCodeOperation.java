package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.EmergencyCode;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.function.Supplier;

final class EmergencyCodeOperation {
    private static final String PATH = "/api/v3/app/codes/device/{id}/type/emergency";

    private EmergencyCodeOperation() {

    }

    public static List<EmergencyCode> call(HttpTransport transport, Supplier<String> tokenSupplier, int id) {
        String path = PATH.replace("{id}", String.valueOf(id));
        Response response = transport.get(Host.HOUSEHOLD, path, tokenSupplier);
        List<EmergencyCodeData> items = SnakeCaseDecoder.dataNode(response, "items", new TypeReference<>() {
        });

        return items.stream().map(EmergencyCodeData::toModel).toList();
    }
}
