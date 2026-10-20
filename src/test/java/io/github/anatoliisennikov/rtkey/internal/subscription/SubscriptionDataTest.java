package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.model.Offer;
import io.github.anatoliisennikov.rtkey.model.Subscription;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionDataTest {
    private static final Instant CREATED_AT = Instant.parse("2025-03-01T00:00:00Z");
    private static final Instant CHANGED_AT = Instant.parse("2025-03-15T00:00:00Z");
    private static final Instant EXPIRED_AT = Instant.parse("2026-03-01T00:00:00Z");

    @Test
    public void toModelMapsEveryField() {
        Subscription subscription = SubscriptionData.toModel(subscriptionData(OfferDataFixtures.offerData()));

        assertThat(subscription).isEqualTo(new Subscription(
                "sub-1",
                "orpon-1",
                "77",
                8801,
                "active",
                "paused",
                299.99,
                CREATED_AT,
                CHANGED_AT,
                true,
                "monthly",
                EXPIRED_AT,
                "parent-1",
                "account-1",
                OfferDataFixtures.offer(),
                199.5
        ));
    }

    @Test
    public void toModelKeepsTheTwoStatusFieldsApart() {
        Subscription subscription = SubscriptionData.toModel(subscriptionData(OfferDataFixtures.offerData()));

        assertThat(subscription.statusType()).isEqualTo("active");
        assertThat(subscription.previousStatusType()).isEqualTo("paused");
        assertThat(subscription.billingType()).isEqualTo("monthly");
    }

    @Test
    public void toModelKeepsTheTwoInstantsApart() {
        Subscription subscription = SubscriptionData.toModel(subscriptionData(OfferDataFixtures.offerData()));

        assertThat(subscription.createdAt()).isEqualTo(CREATED_AT);
        assertThat(subscription.changedAt()).isEqualTo(CHANGED_AT);
        assertThat(subscription.expiredAt()).isEqualTo(EXPIRED_AT);
    }

    @Test
    public void toModelMapsNestedOfferDownToItsPlanOptions() {
        Subscription subscription = SubscriptionData.toModel(subscriptionData(OfferDataFixtures.offerData()));

        Offer offer = Objects.requireNonNull(subscription.offer(), "a present offer must be mapped");
        Offer.Plan plan = Objects.requireNonNull(offer.plan(), "a present plan must be mapped");

        assertThat(offer).isEqualTo(OfferDataFixtures.offer());
        assertThat(plan).isEqualTo(OfferDataFixtures.plan());
        assertThat(plan.optionDescriptions())
                .containsExactlyElementsOf(OfferDataFixtures.optionDescriptions());
    }

    @Test
    public void toModelKeepsAbsentOfferNull() {
        Subscription subscription = SubscriptionData.toModel(subscriptionData(null));

        assertThat(subscription.offer()).isNull();
        assertThat(subscription.offerId()).isEqualTo(8801);
        assertThat(subscription.id()).isEqualTo("sub-1");
    }

    @Test
    public void toModelKeepsNullableScalarsAbsent() {
        Subscription subscription = SubscriptionData.toModel(subscriptionDataWithoutNullableScalars(null));

        assertThat(subscription.price()).isNull();
        assertThat(subscription.changedAt()).isNull();
        assertThat(subscription.expiredAt()).isNull();
        assertThat(subscription.parentId()).isNull();
        assertThat(subscription.account()).isNull();
        assertThat(subscription.discountPrice()).isNull();
        assertThat(subscription.offer()).isNull();
        assertThat(subscription.createdAt()).isEqualTo(CREATED_AT);
        assertThat(subscription.flatNumber()).isEqualTo("77");
    }

    private static SubscriptionData subscriptionData(@Nullable OfferData offer) {
        return new SubscriptionData(
                "sub-1",
                "orpon-1",
                "77",
                8801,
                "active",
                "paused",
                299.99,
                CREATED_AT,
                CHANGED_AT,
                true,
                "monthly",
                EXPIRED_AT,
                "parent-1",
                "account-1",
                offer,
                199.5
        );
    }

    private static SubscriptionData subscriptionDataWithoutNullableScalars(@Nullable OfferData offer) {
        return new SubscriptionData(
                "sub-1",
                "orpon-1",
                "77",
                8801,
                "active",
                "paused",
                null,
                CREATED_AT,
                null,
                true,
                "monthly",
                null,
                null,
                null,
                offer,
                null
        );
    }
}
