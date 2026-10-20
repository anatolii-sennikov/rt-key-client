package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.model.Company;
import io.github.anatoliisennikov.rtkey.model.User;

/**
 * Account operations of the current user.
 *
 * <p>Obtained from {@link RtKeyClient#account()}.
 *
 * <p>Every method sends an authenticated request through the client that created this service.
 * Failures are reported as {@link io.github.anatoliisennikov.rtkey.exception.RtKeyApiException},
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException} or
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyContractException}; calling a method
 * after the client is closed throws {@link IllegalStateException}.
 */
public interface AccountService {
    /**
     * Returns the current user with the company and the available flats.
     *
     * @return the current user
     */
    User user();

    /**
     * Returns the management company of a flat.
     *
     * @param flatId the flat identifier
     * @return the management company
     * @throws IllegalArgumentException if {@code flatId} is not positive
     */
    Company company(int flatId);
}
