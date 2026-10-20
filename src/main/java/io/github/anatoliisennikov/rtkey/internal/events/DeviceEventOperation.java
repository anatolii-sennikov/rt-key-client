package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.DeviceEvent;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.function.Supplier;

final class DeviceEventOperation {
    private static final String PATH = "/api/v1/app/devices/{id}/events";

    private DeviceEventOperation() {
    }

    public static List<DeviceEvent> call(HttpTransport transport, Supplier<String> tokenSupplier, int deviceId) {
        String path = PATH.replace("{id}", String.valueOf(deviceId));

        Response response = transport.get(Host.HOUSEHOLD, path, tokenSupplier);

        List<DeviceEventData> deviceEventData = SnakeCaseDecoder.rootNode(response, "events",
                new TypeReference<>() {
                });

        return deviceEventData.stream().map(DeviceEventData::toModel).toList();
    }
}
