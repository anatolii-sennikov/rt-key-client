package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.model.Offer;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

final class OfferFixtures {
    static final List<Integer> RF_IDS = List.of(
            2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
            30, 31, 32, 33, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56,
            57, 58, 61, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 88, 8801, 8803, 8804, 8805, 8806, 8807,
            8808, 8809, 8810, 8811, 8812, 8813, 8814, 8815, 8816, 8817, 8818, 9999
    );

    private static final String OFFER_URL = "https://key.rt.ru/documents/oferta.pdf";
    private static final String ICON_TYPE = "ICON_TYPE_UNKNOWN";
    private static final String PLAN_TYPE = "TYPE_UNKNOWN";

    private OfferFixtures() {
    }

    static Offer.Plan expectedPlan(int id, String title, @Nullable String description, String createdAt,
                                    @Nullable String changedAt, @Nullable String summary,
                                    List<String> privilegeTypes) {
        return new Offer.Plan(
                id,
                title,
                description,
                Instant.parse(createdAt),
                changedAt == null ? null : Instant.parse(changedAt),
                List.of(),
                RF_IDS,
                summary,
                ICON_TYPE,
                List.of(),
                privilegeTypes,
                PLAN_TYPE
        );
    }

    static Offer expectedOffer(int id, String type, String orpon, int companyId, int planId,
                               boolean isEnableAutoSubscribe, String statusType, @Nullable Double price,
                               boolean isDigitalWay, String createdAt, @Nullable String changedAt,
                               Offer.Plan plan) {
        return new Offer(
                id,
                type,
                orpon,
                companyId,
                planId,
                isEnableAutoSubscribe,
                statusType,
                price,
                isDigitalWay,
                Instant.parse(createdAt),
                changedAt == null ? null : Instant.parse(changedAt),
                OFFER_URL,
                plan,
                null,
                List.of(),
                null
        );
    }
}
