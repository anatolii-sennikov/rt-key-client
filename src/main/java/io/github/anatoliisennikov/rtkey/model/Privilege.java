package io.github.anatoliisennikov.rtkey.model;

/**
 * A privilege of a flat as returned by
 * {@link io.github.anatoliisennikov.rtkey.SubscriptionService#privileges()}.
 *
 * @param orpon      ORPON identifier
 * @param flatNumber flat number
 * @param type       privilege type reported by the server; a server-defined code, not mapped
 *                   to an enum
 * @param stateType  privilege state type reported by the server; a server-defined code, not mapped
 *                   to an enum
 */
public record Privilege(
        String orpon,
        String flatNumber,
        String type,
        String stateType
) {
}
