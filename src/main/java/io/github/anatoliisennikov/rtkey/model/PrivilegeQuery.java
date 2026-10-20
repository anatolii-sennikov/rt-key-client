package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Filter for the privilege list, passed to
 * {@link io.github.anatoliisennikov.rtkey.SubscriptionService#privileges(PrivilegeQuery)}.
 *
 * <p>All components are optional: an empty query applies no filters. When set, {@code limit} must
 * be positive. The lists are copied and immutable.
 *
 * @param limit       maximum number of privileges, must be positive when set
 * @param flatNumbers flat numbers to filter by (sent as {@code filter.flatNumbers} parameters)
 * @param orpons      ORPON identifiers to filter by (sent as {@code filter.orpons} parameters)
 */
public record PrivilegeQuery(
        @Nullable Integer limit,
        List<String> flatNumbers,
        List<String> orpons

) {
    public PrivilegeQuery {
        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("limit must be positive, got " + limit);
        }
        flatNumbers = List.copyOf(flatNumbers);
        orpons = List.copyOf(orpons);
    }

    /**
     * Creates a query with no filters.
     *
     * @return a new query with all components empty
     */
    public static PrivilegeQuery empty() {
        return new PrivilegeQuery(null, List.of(), List.of());
    }

    /**
     * Creates a new builder for {@link PrivilegeQuery}.
     *
     * @return a new {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable Integer limit;
        private List<String> flatNumbers = List.of();
        private List<String> orpons = List.of();

        private Builder() {
        }

        /**
         * Sets the maximum number of privileges.
         *
         * @param limit the maximum number of privileges, must be positive
         * @return this builder
         */
        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Sets the flat numbers to filter by.
         *
         * @param flatNumbers the flat numbers, sent as {@code filter.flatNumbers} parameters
         * @return this builder
         */
        public Builder flatNumbers(String... flatNumbers) {
            this.flatNumbers = List.of(flatNumbers);
            return this;
        }

        /**
         * Sets the flat numbers to filter by.
         *
         * @param flatNumbers the flat numbers, sent as {@code filter.flatNumbers} parameters
         * @return this builder
         */
        public Builder flatNumbers(Collection<String> flatNumbers) {
            this.flatNumbers = List.copyOf(flatNumbers);
            return this;
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

        public PrivilegeQuery build() {
            return new PrivilegeQuery(limit, flatNumbers, orpons);
        }
    }

}
