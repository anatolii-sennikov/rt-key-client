package io.github.anatoliisennikov.rtkey.internal.device;


import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Intercom;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.function.Supplier;

final class IntercomsOperation {
    private static final String PATH = "/api/v2/app/devices/intercom";

    private IntercomsOperation() {

    }

    public static List<Intercom> call(HttpTransport transport, Supplier<String> tokenSupplier) {
        Response response = transport.get(Host.HOUSEHOLD, PATH, tokenSupplier);
        List<IntercomData> intercomData = SnakeCaseDecoder.dataNode(response, "devices", new TypeReference<>() {
        });

        return intercomData.stream().map(IntercomData::toModel).toList();
    }

}
