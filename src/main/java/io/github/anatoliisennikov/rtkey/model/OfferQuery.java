package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Filter for the offer list, passed to
 * {@link io.github.anatoliisennikov.rtkey.SubscriptionService#offers(OfferQuery)}.
 *
 * <p>All components are optional: an empty query applies no filters. When set, {@code limit} must
 * be positive. The lists are copied and immutable.
 *
 * @param limit  maximum number of offers, must be positive when set
 * @param orpons ORPON identifiers to filter by (sent as {@code filter.orpons} parameters)
 * @param types  offer types to filter by (sent as {@code filter.types} parameters)
 */
public record OfferQuery(
        @Nullable Integer limit,
        List<String> orpons,
        List<OfferType> types

) {
    public OfferQuery {
        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("limit must be positive, got " + limit);
        }
        orpons = List.copyOf(orpons);
        types = List.copyOf(types);
    }

    /**
     * Creates a query with no filters.
     *
     * @return a new query with all components empty
     */
    public static OfferQuery empty() {
        return new OfferQuery(null, List.of(), List.of());
    }

    /**
     * Creates a new builder for {@link OfferQuery}.
     *
     * @return a new {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable Integer limit;
        private List<String> orpons = List.of();
        private List<OfferType> types = List.of();

        private Builder() {
        }

        /**
         * Sets the maximum number of offers.
         *
         * @param limit the maximum number of offers, must be positive
         * @return this builder
         */
        public Builder limit(int limit) {
            this.limit = limit;
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

        /**
         * Sets the offer types to filter by.
         *
         * @param types the offer types, sent as {@code filter.types} parameters
         * @return this builder
         */
        public Builder types(OfferType... types) {
            this.types = List.of(types);
            return this;
        }

        /**
         * Sets the offer types to filter by.
         *
         * @param types the offer types, sent as {@code filter.types} parameters
         * @return this builder
         */
        public Builder types(Collection<OfferType> types) {
            this.types = List.copyOf(types);
            return this;
        }

        public OfferQuery build() {
            return new OfferQuery(limit, orpons, types);
        }
    }
}
