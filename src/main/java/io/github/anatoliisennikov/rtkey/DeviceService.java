package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.model.Device;
import io.github.anatoliisennikov.rtkey.model.Intercom;
import io.github.anatoliisennikov.rtkey.model.Page;

import java.util.List;

/**
 * Device operations: device cards, intercoms and door opening.
 *
 * <p>Obtained from {@link RtKeyClient#devices()}.
 *
 * <p>Every method sends an authenticated request.
 * Failures are reported as {@link io.github.anatoliisennikov.rtkey.exception.RtKeyApiException},
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException} or
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyContractException}; calling a method
 * after the client is closed throws {@link IllegalStateException}.
 */
public interface DeviceService {
    /**
     * Returns a page of device cards.
     *
     * @return the page of device cards
     */
    Page<Device> devices();

    /**
     * Returns the page of device cards containing the device with the given identifier.
     *
     * <p>An unknown or inaccessible identifier yields an empty page, not an error.
     *
     * @param id the device identifier
     * @return the page of device cards
     * @throws IllegalArgumentException if {@code id} is not positive
     */
    Page<Device> devices(int id);

    /**
     * Returns the intercoms.
     *
     * @return the intercoms
     */
    List<Intercom> intercoms();

    /**
     * Sets the user-defined name of an intercom.
     *
     * @param id   the intercom identifier
     * @param name the new user-defined name
     * @return the updated intercom
     * @throws IllegalArgumentException if {@code id} is not positive
     * @throws NullPointerException if {@code name} is {@code null}
     */
    Intercom renameIntercom(int id, String name);

    /**
     * Opens the door of a device.
     *
     * @param id the device identifier
     * @throws IllegalArgumentException if {@code id} is not positive
     */
    void open(int id);
}
