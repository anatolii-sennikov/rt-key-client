package io.github.anatoliisennikov.rtkey.internal.codec;

import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CamelCaseDecoder {
    private CamelCaseDecoder() {}

    public static <T> T data(Response response, Class<T> payloadType) {
        Objects.requireNonNull(payloadType, "payloadType");
        return decodeData(response, Json.IDENTITY_MAPPER.constructType(payloadType));
    }

    public static <T> T data(Response response, TypeReference<T> type) {
        Objects.requireNonNull(type, "type");
        return decodeData(response, Json.IDENTITY_MAPPER.constructType(type));
    }

    public static <T> List<T> resultItems(Response response, Class<T> itemType) {
        Objects.requireNonNull(itemType, "itemType");
        return decodeResultItems(response, Json.IDENTITY_MAPPER.constructType(itemType));
    }

    public static <T> List<T> resultItems(Response response, TypeReference<T> itemType) {
        Objects.requireNonNull(itemType, "itemType");
        return decodeResultItems(response, Json.IDENTITY_MAPPER.constructType(itemType));
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    private static <T> T decodeData(Response response, JavaType type) {
        Objects.requireNonNull(response, "response");
        if (!response.success()) {
            throw ServerErrors.of(response);
        }
        JsonNode root = JsonNodes.readObject(Json.IDENTITY_MAPPER, response, "response");
        JsonNode node = JsonNodes.requirePresent(root.path("data"), "data");
        return JsonNodes.toValue(Json.IDENTITY_MAPPER, node, type, "data");
    }

    private static <T> List<T> decodeResultItems(Response response, JavaType itemType) {
        Objects.requireNonNull(response, "response");
        if (!response.success()) {
            throw ServerErrors.of(response);
        }
        JsonNode root = JsonNodes.readArray(Json.IDENTITY_MAPPER, response, "response");
        List<T> items = new ArrayList<>(root.size());
        for (int i = 0; i < root.size(); i++) {
            String label = "result item " + i + ": result.data";
            JsonNode node = JsonNodes.requirePresent(root.get(i).path("result").path("data"), label);
            items.add(JsonNodes.toValue(Json.IDENTITY_MAPPER, node, itemType, label));
        }
        return items;
    }
}
