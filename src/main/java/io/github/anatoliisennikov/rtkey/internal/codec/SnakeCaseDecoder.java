package io.github.anatoliisennikov.rtkey.internal.codec;

import io.github.anatoliisennikov.rtkey.exception.RtKeyContractException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;

import java.util.Objects;

public final class SnakeCaseDecoder {
   private SnakeCaseDecoder () {}

    public static <T> T data(Response response, Class<T> payloadType) {
        Objects.requireNonNull(payloadType, "payloadType");
        return decodeData(response, Json.JSON_MAPPER.constructType(payloadType));
    }

    public static <T> T data(Response response, TypeReference<T> type) {
        Objects.requireNonNull(type, "type");
        return decodeData(response, Json.JSON_MAPPER.constructType(type));
    }

    public static <T> T dataNode(Response response, String nodeName, Class<T> payloadType) {
        Objects.requireNonNull(payloadType, "payloadType");
        return decodeDataNode(response, nodeName, Json.JSON_MAPPER.constructType(payloadType));
    }

    public static <T> T dataNode(Response response, String nodeName, TypeReference<T> type) {
        Objects.requireNonNull(type, "type");
        return decodeDataNode(response, nodeName, Json.JSON_MAPPER.constructType(type));
    }

    public static <T> T rootNode(Response response, String nodeName, Class<T> payloadType) {
        Objects.requireNonNull(payloadType, "payloadType");
        return decodeRootNode(response, nodeName, Json.JSON_MAPPER.constructType(payloadType));
    }

    public static <T> T rootNode(Response response, String nodeName, TypeReference<T> type) {
        Objects.requireNonNull(type, "type");
        return decodeRootNode(response, nodeName, Json.JSON_MAPPER.constructType(type));
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    private static <T> T decodeData(Response response, JavaType type) {
        Objects.requireNonNull(response, "response");
        if (!response.success()) {
            throw ServerErrors.of(response);
        }
        JsonNode root = JsonNodes.readObject(Json.JSON_MAPPER, response, "response");
        JsonNode node = JsonNodes.requirePresent(root.path("data"), "data");
        return JsonNodes.toValue(Json.JSON_MAPPER, node, type, "data");
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    private static <T> T decodeDataNode(Response response, String nodeName, JavaType type) {
        Objects.requireNonNull(response, "response");
        Objects.requireNonNull(nodeName, "nodeName");
        if (!response.success()) {
            throw ServerErrors.of(response);
        }

        JsonNode root = JsonNodes.readObject(Json.JSON_MAPPER, response, "response");
        JsonNode data = JsonNodes.requirePresent(root.path("data"), "data");

        if (!data.isObject()) {
            throw new RtKeyContractException("data is not an object");
        }
        JsonNode node = JsonNodes.requirePresent(data.path(nodeName), "data." + nodeName);
        return JsonNodes.toValue(Json.JSON_MAPPER, node, type, "data." + nodeName);
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    private static <T> T decodeRootNode(Response response, String nodeName, JavaType type) {
        Objects.requireNonNull(response, "response");
        Objects.requireNonNull(nodeName, "nodeName");
        if (!response.success()) {
            throw ServerErrors.of(response);
        }
        JsonNode root = JsonNodes.readObject(Json.JSON_MAPPER, response, "response");
        JsonNode node = JsonNodes.requirePresent(root.path(nodeName), "root." + nodeName);
        return JsonNodes.toValue(Json.JSON_MAPPER, node, type, "root." + nodeName);
    }
}
