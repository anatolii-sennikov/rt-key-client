package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.model.Offer;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

final class OfferDataFixtures {
    static final Instant OFFER_CREATED_AT = Instant.parse("2025-01-10T00:00:00Z");
    static final Instant OFFER_CHANGED_AT = Instant.parse("2025-02-10T00:00:00Z");
    static final Instant PLAN_CREATED_AT = Instant.parse("2024-12-01T00:00:00Z");
    static final Instant PLAN_CHANGED_AT = Instant.parse("2025-01-05T00:00:00Z");
    static final Double PRICE = 299.99;
    static final Double DISCOUNT_PRICE = 199.5;
    static final String OFFER_URL = "https://key.rt.ru/documents/oferta.pdf";

    private OfferDataFixtures() {
    }

    static OfferData offerData() {
        return offerData(planData(), PRICE, OFFER_CHANGED_AT, DISCOUNT_PRICE, "svc-1");
    }

    static OfferData offerData(OfferData.@Nullable Plan plan, @Nullable Double price,
                               @Nullable Instant changedAt, @Nullable Double discountPrice,
                               @Nullable String svcTypeId) {
        return new OfferData(
                8801,
                "paid",
                "orpon-1",
                4321,
                6601,
                true,
                "active",
                price,
                false,
                OFFER_CREATED_AT,
                changedAt,
                OFFER_URL,
                plan,
                discountPrice,
                List.of("requirement-1", "requirement-2"),
                svcTypeId
        );
    }

    static OfferData.Plan planData() {
        return planData("Описание тарифа", "Итого", optionDescriptionData());
    }

    static OfferData.Plan planData(@Nullable String description, @Nullable String summary,
                                   List<OfferData.Plan.OptionDescription> optionDescriptions) {
        return new OfferData.Plan(
                6601,
                "Базовый",
                description,
                PLAN_CREATED_AT,
                PLAN_CHANGED_AT,
                List.of(1, 2),
                List.of(3, 4),
                summary,
                "ICON_TYPE_UNKNOWN",
                optionDescriptions,
                List.of("open_door", "video"),
                "TYPE_UNKNOWN"
        );
    }

    static List<OfferData.Plan.OptionDescription> optionDescriptionData() {
        return List.of(
                new OfferData.Plan.OptionDescription(
                        OfferData.Plan.PlanIconType.FAST, "Быстрый доступ", "Открытие двери"),
                new OfferData.Plan.OptionDescription(
                        OfferData.Plan.PlanIconType.FACE_REC, "Распознавание лица", null),
                new OfferData.Plan.OptionDescription(
                        OfferData.Plan.PlanIconType.SAFE, "Безопасность", "Охрана")
        );
    }

    static Offer offer() {
        return offer(plan());
    }

    static Offer offer(Offer.@Nullable Plan plan) {
        return new Offer(
                8801,
                "paid",
                "orpon-1",
                4321,
                6601,
                true,
                "active",
                PRICE,
                false,
                OFFER_CREATED_AT,
                OFFER_CHANGED_AT,
                OFFER_URL,
                plan,
                DISCOUNT_PRICE,
                List.of("requirement-1", "requirement-2"),
                "svc-1"
        );
    }

    static Offer.Plan plan() {
        return new Offer.Plan(
                6601,
                "Базовый",
                "Описание тарифа",
                PLAN_CREATED_AT,
                PLAN_CHANGED_AT,
                List.of(1, 2),
                List.of(3, 4),
                "Итого",
                "ICON_TYPE_UNKNOWN",
                optionDescriptions(),
                List.of("open_door", "video"),
                "TYPE_UNKNOWN"
        );
    }

    static List<Offer.Plan.OptionDescription> optionDescriptions() {
        return List.of(
                new Offer.Plan.OptionDescription(
                        Offer.Plan.PlanIconType.FAST, "Быстрый доступ", "Открытие двери"),
                new Offer.Plan.OptionDescription(
                        Offer.Plan.PlanIconType.FACE_REC, "Распознавание лица", null),
                new Offer.Plan.OptionDescription(
                        Offer.Plan.PlanIconType.SAFE, "Безопасность", "Охрана")
        );
    }
}
