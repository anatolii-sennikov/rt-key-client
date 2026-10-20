package io.github.anatoliisennikov.rtkey.exception;

import java.io.Serial;

/**
 * Transport-level failure of an HTTP call.
 *
 * <p>Thrown when a request cannot be completed at the transport level: the
 * connection or the request times out, an I/O error occurs, or the calling thread is
 * interrupted while waiting (its interrupt status is restored before this exception
 * is thrown). Also reported by
 * {@link io.github.anatoliisennikov.rtkey.CameraStream#close()} when the stream body
 * cannot be closed. The underlying failure is available via {@link #getCause()}.
 *
 * <p>Instances are immutable.
 */
public final class RtKeyNetworkException extends RtKeyException{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a network exception.
     *
     * @param message the detail message
     */
    public RtKeyNetworkException(String message) {
        super(message);
    }

    /**
     * Creates a network exception with the underlying failure.
     *
     * @param message the detail message
     * @param cause   the underlying transport failure
     */
    public RtKeyNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
