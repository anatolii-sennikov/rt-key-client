package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * A subscription as returned by
 * {@link io.github.anatoliisennikov.rtkey.SubscriptionService#subscriptions()}.
 *
 * @param id                    subscription identifier
 * @param orpon                 ORPON identifier
 * @param flatNumber            flat number
 * @param offerId               offer identifier
 * @param statusType            subscription status type reported by the server; a server-defined
 *                              code, not mapped to an enum
 * @param previousStatusType    previous subscription status type reported by the server;
 *                              a server-defined code, not mapped to an enum
 * @param price                 price, when set
 * @param createdAt             creation timestamp
 * @param changedAt             last change timestamp, when set
 * @param isEnableAutoSubscribe whether auto-subscribe is enabled
 * @param billingType           billing type reported by the server; passed
 *                              through without interpretation
 * @param expiredAt             expiration timestamp, when set
 * @param parentId              reported by the server, when set; passed
 *                              through without interpretation
 * @param account               reported by the server, when set; passed
 *                              through without interpretation
 * @param offer                 offer details, when set
 * @param discountPrice         discount price, when set
 */
public record Subscription(
        String id,
        String orpon,
        String flatNumber,
        Integer offerId,
        String statusType,
        String previousStatusType,
        @Nullable
        Double price,
        Instant createdAt,
        @Nullable
        Instant changedAt,
        Boolean isEnableAutoSubscribe,
        String billingType,
        @Nullable
        Instant expiredAt,
        @Nullable
        String parentId,
        @Nullable
        String account,
        @Nullable
        Offer offer,
        @Nullable
        Double discountPrice
) {
}
