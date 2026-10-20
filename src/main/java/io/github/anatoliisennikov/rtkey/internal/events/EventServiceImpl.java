package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.EventService;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.BellCountQuery;
import io.github.anatoliisennikov.rtkey.model.DeviceEvent;
import io.github.anatoliisennikov.rtkey.model.Event;
import io.github.anatoliisennikov.rtkey.model.EventsQuery;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class EventServiceImpl implements EventService {
    private final HttpTransport transport;
    private final Supplier<String> tokenSupplier;

    public EventServiceImpl(HttpTransport transport, Supplier<String> tokenSupplier) {
        this.transport = transport;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public int bellCount() {
        return BellCountOperation.call(transport, tokenSupplier, BellCountQuery.empty());
    }

    @Override
    public int bellCount(BellCountQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        return BellCountOperation.call(transport, tokenSupplier, query);
    }

    @Override
    public List<DeviceEvent> deviceEvent(int deviceId) {
        if (deviceId <= 0) throw new IllegalArgumentException("deviceId must be positive");
        return DeviceEventOperation.call(transport, tokenSupplier, deviceId);
    }

    @Override
    public int eventsAmount(Instant begin, Instant end) {
        Objects.requireNonNull(begin, "begin must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return EventsAmountOperation.call(transport, tokenSupplier, begin, end, EventsQuery.empty());
    }

    @Override
    public int eventsAmount(Instant begin, Instant end, EventsQuery query) {
        Objects.requireNonNull(begin, "begin must not be null");
        Objects.requireNonNull(end, "end must not be null");
        Objects.requireNonNull(query, "query must not be null");
        return EventsAmountOperation.call(transport, tokenSupplier, begin, end, query);
    }

    @Override
    public List<Event> events(Instant begin, Instant end) {
        Objects.requireNonNull(begin, "begin must not be null");
        Objects.requireNonNull(end, "end must not be null");
        return EventsListOperation.call(transport, tokenSupplier, begin, end, EventsQuery.empty());
    }

    @Override
    public List<Event> events(Instant begin, Instant end, EventsQuery query) {
        Objects.requireNonNull(begin, "begin must not be null");
        Objects.requireNonNull(end, "end must not be null");
        Objects.requireNonNull(query, "query must not be null");
        return EventsListOperation.call(transport, tokenSupplier, begin, end, query);
    }
}
