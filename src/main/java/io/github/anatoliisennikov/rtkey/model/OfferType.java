package io.github.anatoliisennikov.rtkey.model;

/**
 * Offer type accepted by {@link OfferQuery#types()} and reported on {@link Offer#type()}.
 *
 * <p>{@link #wire()} returns the form used by the API.
 */
public enum OfferType {
    TYPE_UNKNOWN("TYPE_UNKNOWN"),
    COMPANY("COMPANY"),
    RTK("RTK"),
    SHARED("SHARED");

    private final String wire;

    OfferType(String wire) {
        this.wire = wire;
    }

    /**
     * Returns the value used by the API.
     *
     * @return the wire value
     */
    public String wire() {
        return wire;
    }
}
