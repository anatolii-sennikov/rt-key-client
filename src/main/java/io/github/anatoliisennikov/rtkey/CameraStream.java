package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A media stream of a camera.
 *
 * <p>Returned by {@link CameraMediaService#live(CameraData)} and
 * {@link CameraMediaService#archive(CameraData, java.time.Instant)}. Close the stream when no
 * longer needed to release the underlying HTTP body.
 */
public class CameraStream implements AutoCloseable {
    private final InputStream body;

    /**
     * Creates a stream over the given body.
     *
     * @param body the stream body
     * @throws NullPointerException if {@code body} is {@code null}
     */
    public CameraStream(InputStream body) {
        this.body = Objects.requireNonNull(body, "body");
    }

    /**
     * Returns the stream body.
     *
     * @return the stream body
     */
    public InputStream body() {
        return body;
    }

    /**
     * Closes the stream body.
     *
     * @throws RtKeyNetworkException if the body cannot be closed
     */
    @Override
    public void close() {
        try {
            body.close();
        } catch (IOException e) {
            throw new RtKeyNetworkException("Failed to close camera stream", e);
        }
    }
}
