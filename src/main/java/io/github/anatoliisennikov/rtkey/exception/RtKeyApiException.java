package io.github.anatoliisennikov.rtkey.exception;

import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.util.List;

/**
 * Unsuccessful response from the RT Key API.
 *
 * <p>Thrown by the SDK when the API returns an unsuccessful HTTP status. Different
 * endpoints return errors in different payload shapes; the SDK normalizes them into
 * this exception, exposing the HTTP status, the server error code, human-readable
 * details, and the request identifier when one is available.
 *
 * <p>Instances are immutable.
 */
public final class RtKeyApiException extends RtKeyException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int status;
    private final @Nullable String serverCode;
    private final List<String> description;

    /**
     * Creates an API error exception.
     *
     * <p>The exception message is composed from {@code serverMessage} and
     * {@code description}; when neither is available it falls back to the HTTP status
     * and, when present, {@code serverCode}.
     *
     * @param status        the HTTP status code of the failed response
     * @param serverCode    the error code from the error response, may be {@code null}
     * @param requestId     the request identifier from the error response, may be {@code null}
     * @param serverMessage the message from the error response, may be {@code null}
     * @param description   human-readable failure details; must not be {@code null},
     *                      may be empty
     */
    public RtKeyApiException(int status, @Nullable String serverCode, @Nullable String requestId,
                             @Nullable String serverMessage, List<String> description) {
        super(message(status, serverCode, serverMessage, description), requestId);
        this.status = status;
        this.serverCode = serverCode;
        this.description = List.copyOf(description);
    }

    /**
     * Returns the HTTP status code of the failed response.
     *
     * @return the HTTP status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Returns the error code provided in the error response.
     *
     * @return the error code, or {@code null} when the response provides none
     */
    @Nullable
    public String getServerCode() {
        return serverCode;
    }

    /**
     * Returns human-readable failure details extracted from the error response, for
     * example field-level validation failures.
     *
     * @return an immutable list of details, possibly empty
     */
    public List<String> getDescription() {
        return description;
    }

    private static String message(int status, @Nullable String serverCode, @Nullable String serverMessage,
                                  List<String> details) {
        if (serverMessage != null) {
            return details.isEmpty() ? serverMessage : serverMessage + ": " + String.join("; ", details);
        }
        if (!details.isEmpty()) {
            return String.join("; ", details);
        }
        return serverCode == null
                ? "API error (HTTP " + status + ")"
                : "API error " + serverCode + " (HTTP " + status + ")";
    }

}
