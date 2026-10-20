package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.EmptyDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;

import java.util.function.Supplier;

final class OpenOperation {
    private static final String PATH = "/api/v2/app/devices/{id}/open";

    private OpenOperation() {

    }

    public static void call(HttpTransport transport, Supplier<String> tokenSupplier, String id) {
        String path = PATH.replace("{id}", id);
        Response response = transport.post(Host.HOUSEHOLD, path, tokenSupplier);
        EmptyDecoder.decode(response);
    }
}
