package io.github.anatoliisennikov.rtkey.model;

/**
 * Screenshot size accepted by
 * {@link io.github.anatoliisennikov.rtkey.CameraMediaService#screenshot(CameraData, ScreenshotSize, java.time.Instant)}.
 *
 * <p>{@link #wire()} returns the form used by the API. The dimensions: {@code small} 178×100,
 * {@code medium} 356×200, {@code original} 1920×1080. The precise variant uses the same size
 * values; the resulting image dimensions are defined by the server.
 */
public enum ScreenshotSize {
    /** Small size: 178×100. */
    SMALL("small"),
    /** Medium size: 356×200. */
    MEDIUM("medium"),
    /** Original size: 1920×1080. */
    ORIGINAL("original");

    private final String wire;

    ScreenshotSize(String wire) {
        this.wire = wire;
    }

    /**
     * Returns the value used by the API.
     *
     * @return the wire value
     */
    public String wire() {
        return wire;
    }
}
