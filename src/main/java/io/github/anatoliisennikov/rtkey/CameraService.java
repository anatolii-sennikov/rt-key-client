package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.model.ArchiveInterval;
import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Camera operations: the camera list and archive availability.
 *
 * <p>Obtained from {@link RtKeyClient#cameras()}.
 *
 * <p>Every method sends an authenticated request through the client that created this service.
 * Failures are reported as {@link io.github.anatoliisennikov.rtkey.exception.RtKeyApiException},
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException} or
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyContractException}; calling a method
 * after the client is closed throws {@link IllegalStateException}.
 */
public interface CameraService {
    /**
     * Returns the dates of the archive calendar of a camera.
     *
     * @param cameraUid the camera UID
     * @return the dates of the archive calendar
     * @throws NullPointerException if {@code cameraUid} is {@code null}
     */
    List<LocalDate> archiveCalendar(UUID cameraUid);

    /**
     * Returns the archive intervals of a camera.
     *
     * @param cameraUid the camera UID
     * @return the archive intervals
     * @throws NullPointerException if {@code cameraUid} is {@code null}
     */
    List<ArchiveInterval> archiveIntervals(UUID cameraUid);

    /**
     * Returns the camera list.
     *
     * @return the cameras
     */
    List<CameraData> cameras();

    /**
     * Returns the camera list paged by the given query.
     *
     * @param query the limit/offset query
     * @return the cameras of the page
     * @throws NullPointerException if {@code query} is {@code null}
     */
    List<CameraData> cameras(LimitOffsetQuery query);
}
