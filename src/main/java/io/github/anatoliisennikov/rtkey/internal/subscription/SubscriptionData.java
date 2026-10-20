package io.github.anatoliisennikov.rtkey.internal.subscription;


import io.github.anatoliisennikov.rtkey.model.Subscription;
import org.jspecify.annotations.Nullable;


import java.time.Instant;

record SubscriptionData(
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
        OfferData offer,
        @Nullable
        Double discountPrice

) {

    static Subscription toModel(SubscriptionData data) {
        OfferData offer = data.offer();

        return new Subscription(
                data.id(),
                data.orpon(),
                data.flatNumber(),
                data.offerId(),
                data.statusType(),
                data.previousStatusType(),
                data.price(),
                data.createdAt(),
                data.changedAt(),
                data.isEnableAutoSubscribe(),
                data.billingType(),
                data.expiredAt(),
                data.parentId(),
                data.account(),
                OfferData.toModel(offer),
                data.discountPrice()
        );
    }
}
