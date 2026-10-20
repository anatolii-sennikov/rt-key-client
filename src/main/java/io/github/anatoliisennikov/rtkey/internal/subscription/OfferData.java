package io.github.anatoliisennikov.rtkey.internal.subscription;


import io.github.anatoliisennikov.rtkey.model.Offer;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

record OfferData(
        Integer id,
        String type,
        String orpon,
        Integer companyId,
        Integer planId,
        Boolean isEnableAutoSubscribe,
        String statusType,
        @Nullable
        Double price,
        Boolean isDigitalWay,
        Instant createdAt,
        @Nullable
        Instant changedAt,
        String offerUrl,
        @Nullable
        Plan plan,
        @Nullable
        Double discountPrice,
        List<String> discountRequirementTypes,
        @Nullable
        String svcTypeId
) {
    record Plan(
            Integer id,
            String title,
            @Nullable
            String description,
            Instant createdAt,
            @Nullable
            Instant changedAt,
            List<Integer> parentIds,
            List<Integer> rfIds,
            @Nullable
            String summary,
            String iconType,
            List<OptionDescription> optionDescriptions,
            List<String> privilegeTypes,
            String type
    ) {
        record OptionDescription(PlanIconType iconType, String title, @Nullable String summary) {
        }

        enum PlanIconType {FAST, FACE_REC, SAFE}
    }

    static @Nullable Offer toModel(@Nullable OfferData data) {
        if (data == null) {
            return null;
        }
        return new Offer(
                data.id(),
                data.type(),
                data.orpon(),
                data.companyId(),
                data.planId(),
                data.isEnableAutoSubscribe(),
                data.statusType(),
                data.price(),
                data.isDigitalWay(),
                data.createdAt(),
                data.changedAt(),
                data.offerUrl(),
                toModel(data.plan()),
                data.discountPrice(),
                data.discountRequirementTypes(),
                data.svcTypeId()
        );
    }

    static Offer.@Nullable Plan toModel(@Nullable Plan plan) {
        if (plan == null) {
            return null;
        }
        return new Offer.Plan(
                plan.id(),
                plan.title(),
                plan.description(),
                plan.createdAt(),
                plan.changedAt(),
                plan.parentIds(),
                plan.rfIds(),
                plan.summary(),
                plan.iconType(),
                plan.optionDescriptions().stream().map(OfferData::toModel).toList(),
                plan.privilegeTypes(),
                plan.type()
        );
    }

    static Offer.Plan.OptionDescription toModel(Plan.OptionDescription description) {
        return new Offer.Plan.OptionDescription(
                Offer.Plan.PlanIconType.valueOf(description.iconType().name()),
                description.title(),
                description.summary()
        );
    }
}
