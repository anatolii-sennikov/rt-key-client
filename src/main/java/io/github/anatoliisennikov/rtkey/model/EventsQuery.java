package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Filter for the events journal, passed to
 * {@link io.github.anatoliisennikov.rtkey.EventService#events(java.time.Instant, java.time.Instant, EventsQuery)}.
 *
 * <p>All components are optional: an empty query applies no filters. When set, {@code limit} must
 * be positive and {@code offset} must not be negative. The lists are copied and immutable.
 *
 * @param limit      maximum number of events, must be positive when set (sent as {@code limit})
 * @param offset     offset, must not be negative when set (sent as {@code offset})
 * @param deviceIds  device identifiers to filter by (sent as {@code device_ids} parameters)
 * @param eventTypes event types to filter by (sent as {@code event_types} parameters)
 */
public record EventsQuery(
        @Nullable Integer limit,
        @Nullable Integer offset,
        List<String> deviceIds,
        List<EventTypes> eventTypes
) {
    public EventsQuery {
        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("limit must be positive, got " + limit);
        }
        if (offset != null && offset < 0) {
            throw new IllegalArgumentException("offset must not be negative, got " + offset);
        }
        deviceIds = List.copyOf(deviceIds);
        eventTypes = List.copyOf(eventTypes);
    }

    /**
     * Creates a query with no filters.
     *
     * @return a new query with all components empty
     */
    public static EventsQuery empty() {
        return new EventsQuery(null, null, List.of(), List.of());
    }

    /**
     * Creates a new builder for {@link EventsQuery}.
     *
     * @return a new {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable Integer limit;
        private @Nullable Integer offset;
        private List<String> deviceIds = List.of();
        private List<EventTypes> eventTypes = List.of();

        private Builder() {
        }

        /**
         * Sets the maximum number of events to return.
         *
         * @param limit the maximum number of events, must be positive when set
         * @return this builder
         */
        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Sets the offset of the first event to return.
         *
         * @param offset the offset, must not be negative when set
         * @return this builder
         */
        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        /**
         * Sets the device identifiers to filter by.
         *
         * @param deviceIds the device identifiers, sent as {@code device_ids} parameters
         * @return this builder
         */
        public Builder deviceIds(String... deviceIds) {
            this.deviceIds = List.of(deviceIds);
            return this;
        }

        /**
         * Sets the device identifiers to filter by.
         *
         * @param deviceIds the device identifiers, sent as {@code device_ids} parameters
         * @return this builder
         */
        public Builder deviceIds(Collection<String> deviceIds) {
            this.deviceIds = List.copyOf(deviceIds);
            return this;
        }

        /**
         * Sets the event types to filter by.
         *
         * @param eventTypes the event types, sent as {@code event_types} parameters
         * @return this builder
         */
        public Builder eventTypes(EventTypes... eventTypes) {
            this.eventTypes = List.of(eventTypes);
            return this;
        }

        /**
         * Sets the event types to filter by.
         *
         * @param eventTypes the event types, sent as {@code event_types} parameters
         * @return this builder
         */
        public Builder eventTypes(Collection<EventTypes> eventTypes) {
            this.eventTypes = List.copyOf(eventTypes);
            return this;
        }

        public EventsQuery build() {
            return new EventsQuery(limit, offset, deviceIds, eventTypes);
        }
    }
}
