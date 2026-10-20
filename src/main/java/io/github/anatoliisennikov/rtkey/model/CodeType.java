package io.github.anatoliisennikov.rtkey.model;

/**
 * Type of a door code.
 *
 * <p>{@link #wire()} returns the form used by the API.
 */
public enum CodeType {
    TEMPORARY("temporary"),
    EMERGENCY("emergency");

    private final String wire;

    CodeType(String wire) {
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
