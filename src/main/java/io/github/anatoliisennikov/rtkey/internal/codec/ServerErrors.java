package io.github.anatoliisennikov.rtkey.internal.codec;

import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

final class ServerErrors {
    private static final String ERROR = "error";
    private static final String REQUEST_ID = "request_id";
    private static final String MESSAGE = "message";
    private static final String REASONS = "reasons";
    private static final String TYPE = "type";
    private static final String DETAILS = "details";
    private static final String DESCRIPTION = "description";
    private static final String CODE = "code";
    private static final String TITLE = "title";
    private static final String METAS = "metas";
    private static final String FIELD = "field";
    private static final String REASON = "reason";
    private static final String PROPERTY = "property";

    private ServerErrors() {
    }

    static RtKeyApiException of(Response response) {
        JsonNode root = treeOrNull(response.body());
        if (root == null) {
            return new RtKeyApiException(response.status(), null, null, null, List.of());
        }
        JsonNode error = root.path(ERROR);
        String requestId = or(text(root, REQUEST_ID), text(error, REQUEST_ID));
        if (error.path(REASONS).isArray()) {
            return reasonError(response.status(), requestId, error);
        }
        if (error.isObject()) {
            return codeError(response.status(), requestId, error);
        }
        if (root.path(METAS).isArray()) {
            return validationError(response.status(), requestId, root);
        }
        return new RtKeyApiException(response.status(), codeOf(root), requestId, text(root, MESSAGE),
                texts(root.path(DETAILS)));
    }

    private static RtKeyApiException reasonError(int status, @Nullable String requestId, JsonNode error) {
        String code = null;
        String message = text(error, MESSAGE);
        List<String> description = new ArrayList<>();
        boolean firstReason = true;
        for (JsonNode reason : error.path(REASONS)) {
            if (firstReason) {
                code = or(codeOf(reason), text(reason, TYPE));
                message = or(text(reason, MESSAGE), message);
                firstReason = false;
            }
            for (JsonNode detail : reason.path(DETAILS)) {
                String failure = join(text(detail, PROPERTY), text(detail, DESCRIPTION));
                if (failure != null) {
                    description.add(failure);
                }
            }
        }
        return new RtKeyApiException(status, code, requestId, message, description);
    }

    private static RtKeyApiException codeError(int status, @Nullable String requestId, JsonNode error) {
        String title = text(error, TITLE);
        String description = text(error, DESCRIPTION);
        String message = or(title, or(description, text(error, MESSAGE)));
        List<String> details = new ArrayList<>();
        if (description != null && !description.equals(message)) {
            details.add(description);
        }
        details.addAll(texts(error.path(DETAILS)));
        return new RtKeyApiException(status, codeOf(error), requestId, message, details);
    }

    private static RtKeyApiException validationError(int status, @Nullable String requestId, JsonNode root) {
        List<String> description = new ArrayList<>();
        for (JsonNode meta : root.path(METAS)) {
            String failure = join(text(meta, FIELD), text(meta, REASON));
            if (failure != null) {
                description.add(failure);
            }
        }
        return new RtKeyApiException(status, null, requestId, text(root, MESSAGE), description);
    }

    private static @Nullable JsonNode treeOrNull(String body) {
        if (body.isBlank()) {
            return null;
        }
        try {
            JsonNode root = Json.JSON_MAPPER.readTree(body);
            return root.isObject() ? root : null;
        } catch (JacksonException e) {
            return null;
        }
    }

    private static @Nullable String text(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isString() ? blankToNull(value.asString()) : null;
    }

    private static @Nullable String codeOf(JsonNode node) {
        JsonNode value = node.path(CODE);
        if (value.isString()) {
            return blankToNull(value.asString());
        }
        return value.isNumber() ? value.asString() : null;
    }

    private static List<String> texts(JsonNode array) {
        List<String> out = new ArrayList<>();
        for (JsonNode item : array) {
            if (item.isString()) {
                String s = blankToNull(item.asString());
                if (s != null) {
                    out.add(s);
                }
            }
        }
        return out;
    }

    private static @Nullable String blankToNull(@Nullable String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private static @Nullable String or(@Nullable String first, @Nullable String second) {
        return first != null ? first : second;
    }

    private static @Nullable String join(@Nullable String field, @Nullable String reason) {
        if (field == null) {
            return reason;
        }
        return reason == null ? field : field + ": " + reason;
    }
}
