package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

/**
 * A door code with its per-device statuses, as returned by
 * {@link io.github.anatoliisennikov.rtkey.CodeService#codes()}.
 *
 * @param id                 code identifier
 * @param code               the code value
 * @param type               code type reported by the server; the API uses the wire values
 *                           {@code temporary} and {@code emergency}
 * @param title              code title, when set
 * @param flatId             identifier of the flat the code belongs to, when set
 * @param companyId          identifier of the company the code belongs to, when set
 * @param ownerType          owner type reported by the server; passed
 *                           through without interpretation
 * @param ownerId            identifier of the owner, when set
 * @param ownerLogin         login of the owner, when set
 * @param ownerPhoneNumber   phone number of the owner, when set
 * @param ownerStatus        status of the owner reported by the server, when set; a server-defined
 *                           code, not mapped to an enum
 * @param creatorId          identifier of the creator, when set
 * @param creatorLogin       login of the creator, when set
 * @param creatorPhoneNumber phone number of the creator, when set
 * @param creatorStatus      status of the creator reported by the server, when set;
 *                           a server-defined code, not mapped to an enum
 * @param statuses           statuses of the code on the devices
 * @param createdAt          creation timestamp
 * @param expiresAt          expiration timestamp, when set
 */
public record Code(
        Integer id,
        String code,
        String type,
        @Nullable
        String title,
        @Nullable
        Integer flatId,
        @Nullable
        Integer companyId,
        String ownerType,
        @Nullable
        Integer ownerId,
        @Nullable
        String ownerLogin,
        @Nullable
        String ownerPhoneNumber,
        @Nullable
        String ownerStatus,
        @Nullable
        Integer creatorId,
        @Nullable
        String creatorLogin,
        @Nullable
        String creatorPhoneNumber,
        @Nullable
        String creatorStatus,
        List<Status> statuses,
        Instant createdAt,
        @Nullable
        Instant expiresAt
) {
    /**
     * Status of a code on a single device.
     *
     * @param deviceId        device identifier
     * @param deviceCompanyId identifier of the device's company
     * @param deviceTitle     device title
     * @param deviceStatus    status of the code on the device reported by the server;
     *                        a server-defined code, not mapped to an enum
     */
    public record Status(
            Integer deviceId,
            Integer deviceCompanyId,
            String deviceTitle,
            String deviceStatus
    ) {

    }
}
