package io.github.anatoliisennikov.rtkey.internal.codec;

import io.github.anatoliisennikov.rtkey.exception.RtKeyContractException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.function.Predicate;

final class JsonNodes {
    private static final int BODY_PREVIEW_LIMIT = 1024;

    private JsonNodes() {}

    static JsonNode readObject(JsonMapper mapper, Response response, String label) {
        return read(mapper, response, label, JsonNode::isObject, "object");
    }

    static JsonNode readArray(JsonMapper mapper, Response response, String label) {
        return read(mapper, response, label, JsonNode::isArray, "array");
    }

    static JsonNode requirePresent(JsonNode node, String label) {
        if (node.isMissingNode() || node.isNull()) {
            throw new RtKeyContractException(label + " is missing or null");
        }
        return node;
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    static <T> T toValue(JsonMapper mapper, JsonNode node, JavaType type, String label) {
        try {
            return mapper.treeToValue(node, type);
        } catch (JacksonException e) {
            throw new RtKeyContractException(label + " does not match " + type, e);
        }
    }

    private static JsonNode read(JsonMapper mapper, Response response, String label,
                                 Predicate<JsonNode> shape, String expected) {
        String body = response.body();
        if (body.isBlank()) {
            throw new RtKeyContractException(label + " has no body");
        }
        try {
            JsonNode root = mapper.readTree(body);
            if (root == null || !shape.test(root)) {
                throw new RtKeyContractException(label + " is not a JSON " + expected);
            }
            return root;
        } catch (JacksonException e) {
            throw new RtKeyContractException(label + " is not JSON",
                    new RuntimeException(responseDetails(response), e));
        }
    }

    private static String responseDetails(Response response) {
        String request = response.request().isBlank() ? "<unknown>" : response.request();
        String contentType = response.contentType().isBlank() ? "<missing>" : response.contentType();
        return "Upstream response diagnostics: request=" + request
                + ", HTTP status=" + response.status()
                + ", Content-Type=" + contentType
                + ", body preview=" + bodyPreview(response.body());
    }

    private static String bodyPreview(String body) {
        String normalized = body.replaceAll("[\\p{Cc}\\s]+", " ").strip();
        int end = Math.min(normalized.length(), BODY_PREVIEW_LIMIT);
        String excerpt = normalized.substring(0, end)
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        return "\"" + excerpt + (normalized.length() > end ? "… [truncated]" : "") + "\"";
    }
}
