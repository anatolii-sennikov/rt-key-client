package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.EventTypes;
import io.github.anatoliisennikov.rtkey.model.EventsQuery;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class EventsAmountOperationTest extends BaseOperationTest {

    @Test
    public void eventsAmount() throws IOException {
        final String data = getResourceAsString("/events/events_amount.json");

        when(transport.get(eq(Host.EVENTS), eq("/api/v2/events/amount"),
                eq(Query.of("begin_raised_at", "2025-11-01T18:06:12Z")
                        .add("end_raised_at", "2025-11-05T18:06:12Z")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        int amount = EventsAmountOperation.call(transport, tokenSupplier, Instant.parse("2025-11-01T18:06:12Z"),
                Instant.parse("2025-11-05T18:06:12Z"),
                EventsQuery.empty());

        assertThat(amount).isEqualTo(43);
    }

    @Test
    public void eventsAmountFiltered() throws IOException {
        final String data = getResourceAsString("/events/events_amount.json");

        when(transport.get(eq(Host.EVENTS), eq("/api/v2/events/amount"),
                eq(Query.of("device_ids", "123")
                        .add("event_types", "api_open_remote")
                        .add("begin_raised_at", "2025-11-01T18:06:12Z")
                        .add("end_raised_at", "2025-11-05T18:06:12Z")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        EventsQuery eventsQuery = EventsQuery.builder().deviceIds("123").eventTypes(EventTypes.API_OPEN_REMOTE).build();

        int amount = EventsAmountOperation.call(transport, tokenSupplier, Instant.parse("2025-11-01T18:06:12Z"),
                Instant.parse("2025-11-05T18:06:12Z"),
                eventsQuery);

        assertThat(amount).isEqualTo(43);
    }

    @Test
    public void eventsAmountWithPaging() throws IOException {
        final String data = getResourceAsString("/events/events_amount.json");

        when(transport.get(eq(Host.EVENTS), eq("/api/v2/events/amount"),
                eq(Query.of("limit", "10")
                        .add("offset", "5")
                        .add("begin_raised_at", "2025-11-01T18:06:12Z")
                        .add("end_raised_at", "2025-11-05T18:06:12Z")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        EventsQuery eventsQuery = EventsQuery.builder().limit(10).offset(5).build();

        int amount = EventsAmountOperation.call(transport, tokenSupplier, Instant.parse("2025-11-01T18:06:12Z"),
                Instant.parse("2025-11-05T18:06:12Z"),
                eventsQuery);

        assertThat(amount).isEqualTo(43);
    }
}

