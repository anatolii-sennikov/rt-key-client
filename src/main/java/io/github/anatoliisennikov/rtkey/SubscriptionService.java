package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.model.*;

import java.util.List;

/**
 * Subscription operations: offers, privileges and subscriptions.
 *
 * <p>Obtained from {@link RtKeyClient#subscriptions()}.
 *
 * <p>Every method sends an authenticated request.
 * Failures are reported as {@link io.github.anatoliisennikov.rtkey.exception.RtKeyApiException},
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException} or
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyContractException}; calling a method
 * after the client is closed throws {@link IllegalStateException}.
 */
public interface SubscriptionService {
    /**
     * Returns the offers.
     *
     * @return the offers
     */
    List<Offer> offers();

    /**
     * Returns the offers filtered by the given query.
     *
     * @param query the filter
     * @return the offers
     * @throws NullPointerException if {@code query} is {@code null}
     */
    List<Offer> offers(OfferQuery query);

    /**
     * Returns the privileges.
     *
     * @return the privileges
     */
    List<Privilege> privileges();

    /**
     * Returns the privileges filtered by the given query.
     *
     * @param query the filter
     * @return the privileges
     * @throws NullPointerException if {@code query} is {@code null}
     */
    List<Privilege> privileges(PrivilegeQuery query);

    /**
     * Returns the subscriptions.
     *
     * @return the subscriptions
     */
    List<Subscription> subscriptions();

    /**
     * Returns the subscriptions filtered by the given query.
     *
     * @param query the filter
     * @return the subscriptions
     * @throws NullPointerException if {@code query} is {@code null}
     */
    List<Subscription> subscriptions(SubscriptionQuery query);
}
