package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

/**
 * An emergency code as returned by
 * {@link io.github.anatoliisennikov.rtkey.CodeService#emergencyCodes(int)}.
 *
 * @param id          code identifier
 * @param code        the code value
 * @param type        code type reported by the server
 * @param title       code title, when set
 * @param flat        flat referenced by the code, when set
 * @param company     company referenced by the code, when set
 * @param ownerType   owner type reported by the server, when set; passed
 *                    through without interpretation
 * @param owner       owner of the code, when set
 * @param creatorType creator type reported by the server; passed through without interpretation
 * @param creator     creator of the code, when set
 * @param createdAt   creation timestamp
 * @param beginAt     begin timestamp reported by the server, when set
 * @param expiresAt   expiration timestamp
 * @param statuses    statuses of the code on the devices
 */
public record EmergencyCode(
        Integer id,
        String code,
        String type,
        @Nullable
        String title,
        @Nullable
        Flat flat,
        @Nullable
        Company company,
        @Nullable
        String ownerType,
        @Nullable
        User owner,
        String creatorType,
        @Nullable
        User creator,
        Instant createdAt,
        @Nullable
        Instant beginAt,
        Instant expiresAt,
        List<Status> statuses
) {
    /**
     * Status of an emergency code on a single device.
     *
     * @param device          device the status belongs to
     * @param statusChangedAt time the status changed
     * @param status          status reported by the server
     */
    public record Status(
            Device device,
            Instant statusChangedAt,
            String status
    ) {

    }

    /**
     * Flat referenced by an emergency code.
     *
     * @param id     flat identifier
     * @param number flat number
     */
    public record Flat(Integer id, String number) {
    }

    /**
     * User referenced by an emergency code.
     *
     * @param id          user identifier
     * @param firstname   first name, when set
     * @param patronym    patronymic, when set
     * @param lastname    last name, when set
     * @param login       login, when set
     * @param phoneNumber phone number, when set
     * @param email       email address, when set
     */
    public record User(
            Integer id,
            @Nullable String firstname,
            @Nullable String patronym,
            @Nullable String lastname,
            @Nullable String login,
            @Nullable String phoneNumber,
            @Nullable String email
    ) {
    }

    /**
     * Device referenced by an emergency code.
     *
     * @param id         device identifier
     * @param company    company the device belongs to
     * @param type       device type reported by the server
     * @param title      device title
     * @param fullCode   full code reported by the server, when set; passed
     *                   through without interpretation
     * @param callNumber call number reported by the server, when set; passed
     *                   through without interpretation
     */
    public record Device(
            Integer id,
            Company company,
            String type,
            String title,
            @Nullable
            String fullCode,
            @Nullable
            String callNumber
    ) {

    }

    /**
     * Company referenced by an emergency code.
     *
     * @param id company identifier
     */
    public record Company(Integer id) {

    }
}
