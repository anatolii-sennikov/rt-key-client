package io.github.anatoliisennikov.rtkey.exception;

import java.io.Serial;

/**
 * Value that does not match the expected contract.
 *
 * <p>Thrown when a server response cannot be interpreted as expected: the body is
 * not valid JSON, a required field is missing or {@code null}, or a field has an
 * unexpected type. Also thrown when the SDK cannot build a stream URI from the
 * camera data.
 *
 */
public final class RtKeyContractException extends RtKeyException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a contract exception.
     *
     * @param message the detail message
     */
    public RtKeyContractException(String message) {
        super(message);
    }

    /**
     * Creates a contract exception with the underlying failure.
     *
     * @param message the detail message
     * @param cause   the underlying failure, for example a parsing error
     */
    public RtKeyContractException(String message, Throwable cause) {
        super(message, cause);
    }
}
