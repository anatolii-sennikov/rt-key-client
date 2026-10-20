package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.EmptyDecoder;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Intercode;

import java.util.function.Supplier;

final class IntercodeOperation {
    private static final String PATH = "/api/v2/app/flats/{id}/intercode";

    private IntercodeOperation() {
    }

    public static Intercode getIntercode(HttpTransport transport, Supplier<String> tokenSupplier, int id) {
        String path = PATH.replace("{id}", String.valueOf(id));
        Response response = transport.get(Host.HOUSEHOLD, path, tokenSupplier);
        IntercodeData intercodeData = SnakeCaseDecoder.dataNode(response, "intercode", IntercodeData.class);

        return IntercodeData.toModel(intercodeData);
    }

    public static void deleteIntercode(HttpTransport transport, Supplier<String> tokenSupplier, int id) {
        String path = PATH.replace("{id}", String.valueOf(id));
        Response response = transport.delete(Host.HOUSEHOLD, path, tokenSupplier);
        EmptyDecoder.decode(response);
    }
}
