package io.github.anatoliisennikov.rtkey.exception;

import org.jspecify.annotations.Nullable;

import java.io.Serial;

/**
 * Base class for the exceptions with which the RT Key SDK reports failed
 * operations.
 *
 * <p>The hierarchy is sealed; every failed API operation is reported as one of:
 * <ul>
 *   <li>{@link RtKeyApiException} - the server responded with an error status</li>
 *   <li>{@link RtKeyNetworkException} - the request failed at the transport level</li>
 *   <li>{@link RtKeyContractException} - data did not match the expected contract</li>
 * </ul>
 *
 * <p>Carries the server request identifier when the server provides one; see
 * {@link #getRequestId()}.
 */
public abstract sealed class RtKeyException extends RuntimeException permits
        RtKeyApiException, RtKeyNetworkException, RtKeyContractException {

    @Nullable
    private final String requestId;

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception without a request identifier.
     *
     * @param message the detail message
     */
    protected RtKeyException(String message) {
        super(message);
        this.requestId = null;
    }

    /**
     * Creates an exception without a request identifier.
     *
     * @param message the detail message
     * @param cause   the underlying failure
     */
    protected RtKeyException(String message, Throwable cause) {
        super(message, cause);
        this.requestId = null;
    }

    /**
     * Creates an exception carrying a request identifier.
     *
     * @param message   the detail message
     * @param requestId the server request identifier, may be {@code null}
     */
    protected RtKeyException(String message, @Nullable String requestId) {
        super(message);
        this.requestId = requestId;
    }

    /**
     * Returns the identifier the server assigned to the failed request.
     *
     * <p>Only {@link RtKeyApiException} carries a request identifier;
     * {@link RtKeyNetworkException} and {@link RtKeyContractException} always
     * return {@code null}.
     *
     * @return the server request identifier, or {@code null} when absent
     */
    @Nullable
    public final String getRequestId() {
        return requestId;
    }

}
