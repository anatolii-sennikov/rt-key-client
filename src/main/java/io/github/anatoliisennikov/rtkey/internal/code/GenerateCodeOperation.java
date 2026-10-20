package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.Json;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.GeneratedCode;

import java.util.List;
import java.util.function.Supplier;

final class GenerateCodeOperation {
    private static final String PATH = "/api/v3/app/devices/codes/generate";

    private GenerateCodeOperation() {

    }

    public static GeneratedCode call(HttpTransport transport, Supplier<String> tokenSupplier, Integer flatId, Integer deviceId) {
        String payload = Json.JSON_MAPPER.writeValueAsString(new GeneratedCodeRequest(flatId, List.of(deviceId)));
        Response response = transport.post(Host.HOUSEHOLD, PATH, Body.json(payload), tokenSupplier);
        GenerateData generateData = SnakeCaseDecoder.data(response, GenerateData.class);

        return GenerateData.toModel(generateData);
    }

    record GeneratedCodeRequest(Integer flatId, List<Integer> devicesIds) {

    }
}
