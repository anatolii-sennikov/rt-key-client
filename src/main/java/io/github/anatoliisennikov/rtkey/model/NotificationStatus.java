package io.github.anatoliisennikov.rtkey.model;

/**
 * Notification status filter values used by {@link BellCountQuery#statusTypes()}.
 *
 * <p>{@link #wire()} returns the form used by the API.
 */
public enum NotificationStatus {
    STATUS_TYPE_UNKNOWN("STATUS_TYPE_UNKNOWN"),
    NEW("NEW"),
    DELIVERED("DELIVERED"),
    VIEWED("VIEWED");

    private final String wire;

    NotificationStatus(String wire) {
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
