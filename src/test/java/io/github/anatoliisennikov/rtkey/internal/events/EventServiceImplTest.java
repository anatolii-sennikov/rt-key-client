package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.BaseServiceTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.model.BellCountQuery;
import io.github.anatoliisennikov.rtkey.model.EventsQuery;
import io.github.anatoliisennikov.rtkey.model.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;

public class EventServiceImplTest extends BaseServiceTest {
    private static final String BELL_COUNT_PATH = "/bell/api/v1/notification/count";
    private static final String DEVICE_EVENTS_PATH = "/api/v1/app/devices/77777/events";
    private static final String EVENTS_AMOUNT_PATH = "/api/v2/events/amount";
    private static final String EVENTS_LIST_PATH = "/api/v2/events/list";

    private static final Instant BEGIN = Instant.parse("2025-11-01T18:06:12Z");
    private static final Instant END = Instant.parse("2025-11-05T18:06:12Z");

    private static final String DEVICE_ID_ERROR_MESSAGE = "deviceId must be positive";

    private static final String TIME_RANGE =
            "begin_raised_at=2025-11-01T18%3A06%3A12Z&end_raised_at=2025-11-05T18%3A06%3A12Z";

    private EventServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new EventServiceImpl(transport, tokenSupplier);
    }

    @Test
    public void bellCountWithoutQueryBindsEmptyFilters() {
        assertReachedTransport(service::bellCount);

        verifyGetEmptyQuery(Host.KEYAPIS, BELL_COUNT_PATH);
    }

    @Test
    public void bellCountWithQueryForwardsFilters() {
        BellCountQuery query = BellCountQuery.builder()
                .orpons("orpon1")
                .roomNumbers("12")
                .statusTypes(NotificationStatus.NEW)
                .build();

        assertReachedTransport(() -> service.bellCount(query));

        verifyGet(Host.KEYAPIS, BELL_COUNT_PATH,
                "filter.orpons=orpon1&filter.roomNumbers=12&filter.statusTypes=NEW");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void deviceEventRejectsNonPositiveDeviceId(int deviceId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.deviceEvent(deviceId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DEVICE_ID_ERROR_MESSAGE);
    }

    @Test
    public void deviceEventBindsDeviceIdIntoPath() {
        assertReachedTransport(() -> service.deviceEvent(77777));

        verifyGet(Host.HOUSEHOLD, DEVICE_EVENTS_PATH);
    }

    @Test
    public void eventsAmountWithoutQueryBindsOnlyTimeRange() {
        assertReachedTransport(() -> service.eventsAmount(BEGIN, END));

        verifyGet(Host.EVENTS, EVENTS_AMOUNT_PATH, TIME_RANGE);
    }

    @Test
    public void eventsAmountWithQueryBindsFiltersBeforeTimeRange() {
        EventsQuery query = EventsQuery.builder().limit(10).build();

        assertReachedTransport(() -> service.eventsAmount(BEGIN, END, query));

        verifyGet(Host.EVENTS, EVENTS_AMOUNT_PATH, "limit=10&" + TIME_RANGE);
    }

    @Test
    public void eventsWithoutQueryBindsOnlyTimeRange() {
        assertReachedTransport(() -> service.events(BEGIN, END));

        verifyGet(Host.EVENTS, EVENTS_LIST_PATH, TIME_RANGE);
    }

    @Test
    public void eventsWithQueryBindsFiltersBeforeTimeRange() {
        EventsQuery query = EventsQuery.builder().limit(10).build();

        assertReachedTransport(() -> service.events(BEGIN, END, query));

        verifyGet(Host.EVENTS, EVENTS_LIST_PATH, "limit=10&" + TIME_RANGE);
    }
}
