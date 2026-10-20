package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.BaseServiceTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.model.OfferQuery;
import io.github.anatoliisennikov.rtkey.model.OfferType;
import io.github.anatoliisennikov.rtkey.model.PrivilegeQuery;
import io.github.anatoliisennikov.rtkey.model.SubscriptionQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubscriptionServiceImplTest extends BaseServiceTest {
    private static final String OFFERS_PATH = "/subscription/api/v1/offer/list";
    private static final String PRIVILEGES_PATH = "/subscription/api/v1/privilege/list";
    private static final String SUBSCRIPTIONS_PATH = "/subscription/api/v1/subscription/list";

    private SubscriptionServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new SubscriptionServiceImpl(transport, tokenSupplier);
    }

    @Test
    public void offersWithoutQueryBindsEmptyFilters() {
        assertReachedTransport(service::offers);

        verifyGetEmptyQuery(Host.KEYAPIS, OFFERS_PATH);
    }

    @Test
    public void offersWithQueryForwardsFilters() {
        OfferQuery query = OfferQuery.builder()
                .limit(10)
                .orpons("orpon1")
                .types(OfferType.RTK)
                .build();

        assertReachedTransport(() -> service.offers(query));

        verifyGet(Host.KEYAPIS, OFFERS_PATH, "paging.limit=10&filter.orpons=orpon1&filter.types=RTK");
    }

    @Test
    public void privilegesWithoutQueryBindsEmptyFilters() {
        assertReachedTransport(service::privileges);

        verifyGetEmptyQuery(Host.KEYAPIS, PRIVILEGES_PATH);
    }

    @Test
    public void privilegesWithQueryForwardsFilters() {
        PrivilegeQuery query = PrivilegeQuery.builder()
                .limit(10)
                .flatNumbers("12")
                .orpons("orpon1")
                .build();

        assertReachedTransport(() -> service.privileges(query));

        verifyGet(Host.KEYAPIS, PRIVILEGES_PATH, "paging.limit=10&filter.flatNumbers=12&filter.orpons=orpon1");
    }

    @Test
    public void subscriptionsWithoutQueryBindsEmptyFilters() {
        assertReachedTransport(service::subscriptions);

        verifyGetEmptyQuery(Host.KEYAPIS, SUBSCRIPTIONS_PATH);
    }

    @Test
    public void subscriptionsWithQueryForwardsFilters() {
        SubscriptionQuery query = SubscriptionQuery.builder()
                .limit(10)
                .flatNumbers("12")
                .orpons("orpon1")
                .build();

        assertReachedTransport(() -> service.subscriptions(query));

        verifyGet(Host.KEYAPIS, SUBSCRIPTIONS_PATH, "paging.limit=10&filter.flatNumbers=12&filter.orpons=orpon1");
    }
}
