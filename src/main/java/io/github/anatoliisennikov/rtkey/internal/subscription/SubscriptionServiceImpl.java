package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.SubscriptionService;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class SubscriptionServiceImpl implements SubscriptionService {
    private final HttpTransport transport;
    private final Supplier<String> tokenSupplier;

    public SubscriptionServiceImpl(HttpTransport transport, Supplier<String> tokenSupplier) {
        this.transport = transport;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public List<Offer> offers() {
        return OffersOperation.call(transport, tokenSupplier, OfferQuery.empty());
    }

    @Override
    public List<Offer> offers(OfferQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        return OffersOperation.call(transport, tokenSupplier, query);
    }

    @Override
    public List<Privilege> privileges() {
        return PrivilegeListOperation.call(transport, tokenSupplier, PrivilegeQuery.empty());
    }

    @Override
    public List<Privilege> privileges(PrivilegeQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        return PrivilegeListOperation.call(transport, tokenSupplier, query);
    }

    @Override
    public List<Subscription> subscriptions() {
        return SubscriptionsOperation.call(transport, tokenSupplier, SubscriptionQuery.empty());
    }

    @Override
    public List<Subscription> subscriptions(SubscriptionQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        return SubscriptionsOperation.call(transport, tokenSupplier, query);
    }
}
