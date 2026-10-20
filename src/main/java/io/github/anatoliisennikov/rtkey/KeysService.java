package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.model.Key;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;
import io.github.anatoliisennikov.rtkey.model.Page;
import io.github.anatoliisennikov.rtkey.model.Rfid;

/**
 * RFID key operations.
 *
 * <p>Obtained from {@link RtKeyClient#keys()}.
 *
 * <p>{@code Key} is the view returned by the {@code /api/v2/app/keys} endpoints;
 * {@code Rfid} is the view returned by the {@code /api/v5/devices/rfids} endpoints.
 *
 * <p>Every method sends an authenticated request.
 * Failures are reported as {@link io.github.anatoliisennikov.rtkey.exception.RtKeyApiException},
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException} or
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyContractException}; calling a method
 * after the client is closed throws {@link IllegalStateException}.
 */
public interface KeysService {
    /**
     * Returns a page of RFID keys.
     *
     * @return the page of RFID keys
     */
    Page<Rfid> rfids();

    /**
     * Returns a page of RFID keys paged by the given query.
     *
     * @param query the limit/offset query
     * @return the page of RFID keys
     * @throws NullPointerException if {@code query} is {@code null}
     */
    Page<Rfid> rfids(LimitOffsetQuery query);

    /**
     * Creates an RFID key for a flat.
     *
     * @param flatId  the flat identifier
     * @param rfidUid the RFID UID
     * @param name    the key name
     * @return the created key
     * @throws IllegalArgumentException if {@code flatId} is not positive
     * @throws NullPointerException if {@code rfidUid} or {@code name} is {@code null}
     */
    Key createKey(int flatId, String rfidUid, String name);

    /**
     * Renames an RFID key.
     *
     * @param keyId the key identifier
     * @param name  the new name
     * @return the updated key
     * @throws IllegalArgumentException if {@code keyId} is not positive
     * @throws NullPointerException if {@code name} is {@code null}
     */
    Key renameKey(int keyId, String name);

    /**
     * Deletes an RFID key.
     *
     * @param rfidId the RFID identifier
     * @throws IllegalArgumentException if {@code rfidId} is not positive
     */
    void deleteRfid(int rfidId);
}
