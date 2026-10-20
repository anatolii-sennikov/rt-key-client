package io.github.anatoliisennikov.rtkey.model;

import java.util.Collection;
import java.util.List;

/**
 * Filter for the bell notification count, passed to
 * {@link io.github.anatoliisennikov.rtkey.EventService#bellCount(BellCountQuery)}.
 *
 * <p>All components are optional: an empty query applies no filters. The lists are copied and
 * immutable.
 *
 * @param orpons      ORPON identifiers to filter by (sent as {@code filter.orpons} parameters)
 * @param roomNumbers flat numbers to filter by (sent as {@code filter.roomNumbers} parameters)
 * @param statusTypes notification statuses to filter by (sent as {@code filter.statusTypes} parameters)
 */
public record BellCountQuery(
        List<String> orpons,
        List<String> roomNumbers,
        List<NotificationStatus> statusTypes
) {
    public BellCountQuery {
        orpons = List.copyOf(orpons);
        roomNumbers = List.copyOf(roomNumbers);
        statusTypes = List.copyOf(statusTypes);
    }

    /**
     * Creates a query with no filters.
     *
     * @return a new query with all components empty
     */
    public static BellCountQuery empty() {
        return new BellCountQuery(List.of(), List.of(), List.of());
    }

    /**
     * Creates a new builder for {@link BellCountQuery}.
     *
     * @return a new {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<String> orpons = List.of();
        private List<String> roomNumbers = List.of();
        private List<NotificationStatus> statusTypes = List.of();

        private Builder() {
        }

        /**
         * Sets the ORPON identifiers to filter by.
         *
         * @param orpons the ORPON identifiers, sent as {@code filter.orpons} parameters
         * @return this builder
         */
        public Builder orpons(String... orpons) {
            this.orpons = List.of(orpons);
            return this;
        }

        /**
         * Sets the ORPON identifiers to filter by.
         *
         * @param orpons the ORPON identifiers, sent as {@code filter.orpons} parameters
         * @return this builder
         */
        public Builder orpons(Collection<String> orpons) {
            this.orpons = List.copyOf(orpons);
            return this;
        }

        /**
         * Sets the flat numbers to filter by.
         *
         * @param roomNumbers the flat numbers, sent as {@code filter.roomNumbers} parameters
         * @return this builder
         */
        public Builder roomNumbers(String... roomNumbers) {
            this.roomNumbers = List.of(roomNumbers);
            return this;
        }

        /**
         * Sets the flat numbers to filter by.
         *
         * @param roomNumbers the flat numbers, sent as {@code filter.roomNumbers} parameters
         * @return this builder
         */
        public Builder roomNumbers(Collection<String> roomNumbers) {
            this.roomNumbers = List.copyOf(roomNumbers);
            return this;
        }

        /**
         * Sets the notification statuses to filter by.
         *
         * @param statusTypes the notification statuses, sent as {@code filter.statusTypes} parameters
         * @return this builder
         */
        public Builder statusTypes(NotificationStatus... statusTypes) {
            this.statusTypes = List.of(statusTypes);
            return this;
        }

        /**
         * Sets the notification statuses to filter by.
         *
         * @param statusTypes the notification statuses, sent as {@code filter.statusTypes} parameters
         * @return this builder
         */
        public Builder statusTypes(Collection<NotificationStatus> statusTypes) {
            this.statusTypes = List.copyOf(statusTypes);
            return this;
        }

        public BellCountQuery build() {
            return new BellCountQuery(orpons, roomNumbers, statusTypes);
        }
    }
}
