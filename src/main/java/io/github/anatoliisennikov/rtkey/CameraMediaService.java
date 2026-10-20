package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.ScreenshotSize;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * Camera media operations: screenshots and live and archive streams.
 *
 * <p>Obtained from {@link RtKeyClient#cameraMedia()}. Requests are authorized with the tokens
 * carried by the {@link CameraData} instance; pass a camera obtained from
 * {@link CameraService#cameras()}.
 *
 * <p>Failures are reported as {@link io.github.anatoliisennikov.rtkey.exception.RtKeyApiException},
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException} or
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyContractException}; calling a method
 * after the client is closed throws {@link IllegalStateException}.
 */
public interface CameraMediaService {
    /**
     * Returns a screenshot of the camera.
     *
     * @param camera the camera
     * @param size   the screenshot size
     * @param at     the moment of the screenshot, or {@code null} to request the latest frame
     * @return the screenshot bytes
     * @throws NullPointerException if {@code camera} or {@code size} is {@code null}
     */
    byte[] screenshot(CameraData camera, ScreenshotSize size, @Nullable Instant at);

    /**
     * Returns a screenshot of the camera built from its precise URL template.
     *
     * <p>The precise variant uses the same size values; the resulting image dimensions are defined
     * by the server.
     *
     * @param camera the camera
     * @param size   the screenshot size
     * @param at     the moment of the screenshot, or {@code null} to request the latest frame
     * @return the screenshot bytes
     * @throws NullPointerException if {@code camera} or {@code size} is {@code null}
     */
    byte[] screenshotPrecise(CameraData camera, ScreenshotSize size, @Nullable Instant at);

    /**
     * Opens the live stream of the camera.
     *
     * @param camera the camera
     * @return the live stream; close it when no longer needed
     * @throws NullPointerException if {@code camera} is {@code null}
     */
    CameraStream live(CameraData camera);

    /**
     * Opens the archive stream of the camera from the given moment.
     *
     * @param camera the camera
     * @param from   the moment to start the archive stream from
     * @return the archive stream; close it when no longer needed
     * @throws NullPointerException if {@code camera} or {@code from} is {@code null}
     */
    CameraStream archive(CameraData camera, Instant from);
}
