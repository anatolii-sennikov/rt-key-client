package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Device;
import io.github.anatoliisennikov.rtkey.model.Page;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.type.TypeReference;

import java.util.function.Supplier;

final class DeviceOperation {
    private static final String PATH = "/api/v4/devices";

    private DeviceOperation() {

    }

    public static Page<Device> call(HttpTransport transport, Supplier<String> tokenSupplier, @Nullable Integer id) {
        Query query = id == null ? Query.empty() : Query.of("ids", String.valueOf(id));

        Response response = transport.get(Host.HOUSEHOLD, PATH, query, tokenSupplier);
        Page<DeviceV4> page = SnakeCaseDecoder.data(response, new TypeReference<>() {
        });

        return new Page<>(page.items().stream().map(DeviceV4::toModel).toList(), page.total());
    }

}
