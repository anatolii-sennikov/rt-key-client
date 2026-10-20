package io.github.anatoliisennikov.rtkey.model;

/**
 * Event types accepted by the events query ({@link EventsQuery#eventTypes()}).
 *
 * <p>{@link #wire()} returns the form used by the API.
 */
public enum EventTypes {
    API_OPEN_REMOTE("api_open_remote"),
    FACE_OPEN_REMOTE("face_open_remote"),
    PIN_CODE_OPEN_REMOTE("pin_code_open_remote"),
    DTMF_OPEN_REMOTE("dtmf_open_remote"),
    EMERGENCY_MODE_ON_REMOTE("emergency_mode_on_remote"),
    EMERGENCY_MODE_OFF_REMOTE("emergency_mode_off_remote"),
    CODE_OPEN_LOCAL("code_open_local"),
    RFID_OPEN_LOCAL("rfid_open_local"),
    EMERGENCY_ALARM_REMOTE("emergency_alarm_remote"),
    EMERGENCY_ALARM_OFF_REMOTE("emergency_alarm_off_remote"),
    EMERGENCY_ALARM_ON_REMOTE("emergency_alarm_on_remote"),
    GET_CREDENTIALS("get_credentials"),
    CHANGE_PASSWORD("change_password"),
    USER_ENTRY("user_entry"),
    DTMF_OPEN_LOCAL("dtmf_open_local"),
    CAR_NUMBER_OPEN_REMOTE("car_number_open_remote"),
    DISPATCHER_OPEN("dispatcher_open"),
    DISPATCHER_DENIED_OPEN("dispatcher_denied_open"),
    VEHICLE_TYPE_OPEN_REMOTE("vehicle_type_open_remote");

    private final String wire;

    EventTypes(String wire) {
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
