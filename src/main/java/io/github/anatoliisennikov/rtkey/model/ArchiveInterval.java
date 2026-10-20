package io.github.anatoliisennikov.rtkey.model;

import java.time.Instant;

/**
 * A single archive interval of a camera.
 *
 * <p>Returned by {@link io.github.anatoliisennikov.rtkey.CameraService#archiveIntervals(java.util.UUID)},
 * which converts the epoch-second pairs reported by the server into {@link Instant} values.
 *
 * @param start start of the interval
 * @param end   end of the interval
 */
public record ArchiveInterval(Instant start, Instant end) {
}
