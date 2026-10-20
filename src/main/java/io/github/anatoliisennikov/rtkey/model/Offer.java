package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

/**
 * An offer as returned by {@link io.github.anatoliisennikov.rtkey.SubscriptionService#offers()}.
 *
 * @param id                       offer identifier
 * @param type                     offer type reported by the server; the values are listed in
 *                                 {@link OfferType}
 * @param orpon                    ORPON identifier
 * @param companyId                company identifier
 * @param planId                   plan identifier
 * @param isEnableAutoSubscribe    whether auto-subscribe is enabled
 * @param statusType               offer status type reported by the server; a server-defined code,
 *                                 not mapped to an enum
 * @param price                    price, when set
 * @param isDigitalWay             reported by the server; passed through without interpretation
 * @param createdAt                creation timestamp
 * @param changedAt                last change timestamp, when set
 * @param offerUrl                 offer URL reported by the server
 * @param plan                     plan details, when set
 * @param discountPrice            discount price, when set
 * @param discountRequirementTypes reported by the server; passed through without interpretation
 * @param svcTypeId                reported by the server; passed through without interpretation
 */
public record Offer(
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
    /**
     * Plan of an offer.
     *
     * @param id                 plan identifier
     * @param title              plan title
     * @param description        plan description, when set
     * @param createdAt          creation timestamp
     * @param changedAt          last change timestamp, when set
     * @param parentIds          reported by the server; passed through without interpretation
     * @param rfIds              reported by the server; passed through without interpretation
     * @param summary            plan summary, when set
     * @param iconType           icon type reported by the server; a server-defined code, not mapped
     *                           to an enum
     * @param optionDescriptions option descriptions
     * @param privilegeTypes     privilege types reported by the server; passed
     *                           through without interpretation
     * @param type               reported by the server; passed through without interpretation
     */
    public record Plan(
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
        /**
         * Description of a plan option.
         *
         * @param iconType icon type of the option
         * @param title    option title
         * @param summary  option summary, when set
         */
        public record OptionDescription(PlanIconType iconType, String title, @Nullable String summary) {
        }

        /**
         * Icon type of a plan option.
         */
        public enum PlanIconType { FAST, FACE_REC, SAFE }
    }
}
