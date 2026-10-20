package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

/**
 * A limit/offset query used by list endpoints, for example
 * {@link io.github.anatoliisennikov.rtkey.CameraService#cameras(LimitOffsetQuery)} and
 * {@link io.github.anatoliisennikov.rtkey.KeysService#rfids(LimitOffsetQuery)}.
 *
 * <p>Both components are optional. When set, {@code limit} must be positive and {@code offset}
 * must not be negative.
 *
 * @param limit  maximum number of items, must be positive when set
 * @param offset offset, must not be negative when set
 */
public record LimitOffsetQuery(
        @Nullable Integer limit,
        @Nullable Integer offset
) {
    public LimitOffsetQuery {
        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("limit must be positive, got " + limit);
        }

        if (offset != null && offset < 0) {
            throw new IllegalArgumentException("offset must not be negative, got " + offset);
        }
    }

    /**
     * Creates a query with no limit and no offset.
     *
     * @return a new query with both components {@code null}
     */
    public static LimitOffsetQuery empty() {
        return new LimitOffsetQuery(null, null);
    }

    /**
     * Creates a new builder for {@link LimitOffsetQuery}.
     *
     * @return a new {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable Integer limit;
        private @Nullable Integer offset;

        private Builder() {
        }

        /**
         * Sets the maximum number of items.
         *
         * @param limit the maximum number of items, must be positive
         * @return this builder
         */
        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Sets the offset.
         *
         * @param offset the offset, must not be negative
         * @return this builder
         */
        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public LimitOffsetQuery build() {
            return new LimitOffsetQuery(limit, offset);
        }
    }
}
