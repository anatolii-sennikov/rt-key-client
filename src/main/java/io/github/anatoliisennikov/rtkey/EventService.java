package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.model.BellCountQuery;
import io.github.anatoliisennikov.rtkey.model.DeviceEvent;
import io.github.anatoliisennikov.rtkey.model.Event;
import io.github.anatoliisennikov.rtkey.model.EventsQuery;

import java.time.Instant;
import java.util.List;

/**
 * Event operations: the events journal, per-device event feeds and the bell notification count.
 *
 * <p>Obtained from {@link RtKeyClient#events()}.
 *
 * <p>Every method sends an authenticated request through the client that created this service.
 * Failures are reported as {@link io.github.anatoliisennikov.rtkey.exception.RtKeyApiException},
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException} or
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyContractException}; calling a method
 * after the client is closed throws {@link IllegalStateException}.
 */
public interface EventService {
    /**
     * Returns the bell notification count.
     *
     * @return the count
     */
    int bellCount();

    /**
     * Returns the bell notification count for the given filter.
     *
     * @param query the filter
     * @return the count
     * @throws NullPointerException if {@code query} is {@code null}
     */
    int bellCount(BellCountQuery query);

    /**
     * Returns the short event feed of a device.
     *
     * @param deviceId the device identifier
     * @return the events of the feed
     * @throws IllegalArgumentException if {@code deviceId} is not positive
     */
    List<DeviceEvent> deviceEvent(int deviceId);

    /**
     * Returns the number of events in the interval.
     *
     * @param begin start of the interval
     * @param end   end of the interval
     * @return the number of events
     * @throws NullPointerException if {@code begin} or {@code end} is {@code null}
     */
    int eventsAmount(Instant begin, Instant end);

    /**
     * Returns the number of events in the interval for the given filter.
     *
     * @param begin start of the interval
     * @param end   end of the interval
     * @param query the filter
     * @return the number of events
     * @throws NullPointerException if {@code begin}, {@code end} or {@code query} is {@code null}
     */
    int eventsAmount(Instant begin, Instant end, EventsQuery query);

    /**
     * Returns the events in the interval.
     *
     * @param begin start of the interval
     * @param end   end of the interval
     * @return the events
     * @throws NullPointerException if {@code begin} or {@code end} is {@code null}
     */
    List<Event> events(Instant begin, Instant end);

    /**
     * Returns the events in the interval for the given filter.
     *
     * @param begin start of the interval
     * @param end   end of the interval
     * @param query the filter
     * @return the events
     * @throws NullPointerException if {@code begin}, {@code end} or {@code query} is {@code null}
     */
    List<Event> events(Instant begin, Instant end, EventsQuery query);
}
