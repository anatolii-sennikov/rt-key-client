package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.model.Offer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static io.github.anatoliisennikov.rtkey.internal.subscription.OfferDataFixtures.offer;
import static io.github.anatoliisennikov.rtkey.internal.subscription.OfferDataFixtures.offerData;
import static io.github.anatoliisennikov.rtkey.internal.subscription.OfferDataFixtures.optionDescriptionData;
import static io.github.anatoliisennikov.rtkey.internal.subscription.OfferDataFixtures.optionDescriptions;
import static io.github.anatoliisennikov.rtkey.internal.subscription.OfferDataFixtures.plan;
import static io.github.anatoliisennikov.rtkey.internal.subscription.OfferDataFixtures.planData;
import static org.assertj.core.api.Assertions.assertThat;

public class OfferDataTest {

    @Test
    public void toModelMapsEveryFieldOfOfferAndPlan() {
        assertThat(requireOffer(offerData())).isEqualTo(offer());
    }

    @Test
    public void toModelReturnsNullForAbsentOffer() {
        assertThat(OfferData.toModel((OfferData) null)).isNull();
    }

    @Test
    public void toModelKeepsAbsentPlanNull() {
        Offer mapped = requireOffer(offerData(null, 299.99, OfferDataFixtures.OFFER_CHANGED_AT, 199.5, "svc-1"));

        assertThat(mapped).isEqualTo(offer(null));
        assertThat(mapped.plan()).isNull();
    }

    @Test
    public void toModelKeepsNullableScalarsAbsent() {
        Offer mapped = requireOffer(offerData(planData(), null, null, null, null));

        assertThat(mapped.price()).isNull();
        assertThat(mapped.changedAt()).isNull();
        assertThat(mapped.discountPrice()).isNull();
        assertThat(mapped.svcTypeId()).isNull();
        assertThat(mapped.id()).isEqualTo(8801);
        assertThat(mapped.type()).isEqualTo("paid");
        assertThat(mapped.orpon()).isEqualTo("orpon-1");
        assertThat(mapped.companyId()).isEqualTo(4321);
        assertThat(mapped.planId()).isEqualTo(6601);
        assertThat(mapped.statusType()).isEqualTo("active");
        assertThat(mapped.offerUrl()).isEqualTo(OfferDataFixtures.OFFER_URL);
        assertThat(mapped.discountRequirementTypes()).containsExactly("requirement-1", "requirement-2");
        assertThat(mapped.plan()).isEqualTo(plan());
    }

    @Test
    public void toModelKeepsFlagsApart() {
        Offer mapped = requireOffer(offerData());

        assertThat(mapped.isEnableAutoSubscribe()).isTrue();
        assertThat(mapped.isDigitalWay()).isFalse();
    }

    @Test
    public void toModelPlanMapsEveryField() {
        assertThat(requirePlan(planData())).isEqualTo(plan());
    }

    @Test
    public void toModelPlanReturnsNullForAbsentPlan() {
        assertThat(OfferData.toModel((OfferData.Plan) null)).isNull();
    }

    @Test
    public void toModelPlanKeepsNullableFieldsAbsent() {
        Offer.Plan mapped = requirePlan(planData(null, null, optionDescriptionData()));

        assertThat(mapped.description()).isNull();
        assertThat(mapped.summary()).isNull();
        assertThat(mapped.title()).isEqualTo("Базовый");
        assertThat(mapped.parentIds()).containsExactly(1, 2);
        assertThat(mapped.rfIds()).containsExactly(3, 4);
        assertThat(mapped.privilegeTypes()).containsExactly("open_door", "video");
    }

    @Test
    public void toModelPlanKeepsEmptyOptionDescriptionsEmpty() {
        Offer.Plan mapped = requirePlan(planData("Описание тарифа", "Итого", List.of()));

        assertThat(mapped.optionDescriptions()).isEmpty();
    }

    @Test
    public void toModelConvertsEveryPlanIconTypeByName() {
        Offer.Plan mapped = requirePlan(planData());

        assertThat(mapped.optionDescriptions())
                .extracting(description -> description.iconType())
                .containsExactly(
                        Offer.Plan.PlanIconType.FAST,
                        Offer.Plan.PlanIconType.FACE_REC,
                        Offer.Plan.PlanIconType.SAFE
                );
        assertThat(mapped.optionDescriptions()).containsExactlyElementsOf(optionDescriptions());
    }

    @Test
    public void toModelOptionDescriptionKeepsTitleAndNullableSummary() {
        Offer.Plan.OptionDescription mapped = OfferData.toModel(new OfferData.Plan.OptionDescription(
                OfferData.Plan.PlanIconType.FACE_REC, "Распознавание лица", null));

        assertThat(mapped.iconType()).isEqualTo(Offer.Plan.PlanIconType.FACE_REC);
        assertThat(mapped.title()).isEqualTo("Распознавание лица");
        assertThat(mapped.summary()).isNull();
    }

    private static Offer requireOffer(OfferData data) {
        return Objects.requireNonNull(OfferData.toModel(data), "a present offer must be mapped");
    }

    private static Offer.Plan requirePlan(OfferData.Plan plan) {
        return Objects.requireNonNull(OfferData.toModel(plan), "a present plan must be mapped");
    }
}
