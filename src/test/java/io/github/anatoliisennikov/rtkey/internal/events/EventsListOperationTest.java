package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Event;
import io.github.anatoliisennikov.rtkey.model.EventTypes;
import io.github.anatoliisennikov.rtkey.model.EventsQuery;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class EventsListOperationTest extends BaseOperationTest {

    @Test
    public void eventList() throws IOException {
        final String data = getResourceAsString("/events/events_list.json");

        when(transport.get(eq(Host.EVENTS), eq("/api/v2/events/list"),
                eq(Query.of("begin_raised_at", "2025-11-01T18:06:12Z")
                        .add("end_raised_at", "2025-11-05T18:06:12Z")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<Event> events = EventsListOperation.call(transport, tokenSupplier, Instant.parse("2025-11-01T18:06:12Z"),
                Instant.parse("2025-11-05T18:06:12Z"),
                EventsQuery.empty());

        assertThat(events).isEqualTo(expectedEvents());
    }

    @Test
    public void eventListFiltered() throws IOException {
        final String data = getResourceAsString("/events/events_list.json");

        when(transport.get(eq(Host.EVENTS), eq("/api/v2/events/list"),
                eq(Query.of("device_ids", "123")
                        .add("event_types", "api_open_remote")
                        .add("begin_raised_at", "2025-11-01T18:06:12Z")
                        .add("end_raised_at", "2025-11-05T18:06:12Z")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));


        EventsQuery eventsQuery = EventsQuery.builder().deviceIds("123").eventTypes(EventTypes.API_OPEN_REMOTE).build();

        List<Event> events = EventsListOperation.call(transport, tokenSupplier, Instant.parse("2025-11-01T18:06:12Z"),
                Instant.parse("2025-11-05T18:06:12Z"),
                eventsQuery);

        assertThat(events).isEqualTo(expectedEvents());
    }

    @Test
    public void eventListWithPaging() throws IOException {
        final String data = getResourceAsString("/events/events_list.json");

        when(transport.get(eq(Host.EVENTS), eq("/api/v2/events/list"),
                eq(Query.of("limit", "10")
                        .add("offset", "5")
                        .add("begin_raised_at", "2025-11-01T18:06:12Z")
                        .add("end_raised_at", "2025-11-05T18:06:12Z")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        EventsQuery eventsQuery = EventsQuery.builder().limit(10).offset(5).build();

        List<Event> events = EventsListOperation.call(transport, tokenSupplier, Instant.parse("2025-11-01T18:06:12Z"),
                Instant.parse("2025-11-05T18:06:12Z"),
                eventsQuery);

        assertThat(events).isEqualTo(expectedEvents());
    }

    @Test
    public void eventListDecodesEveryWireField() throws IOException {
        final String data = getResourceAsString("/events/events_list_all_fields.json");

        when(transport.get(eq(Host.EVENTS), eq("/api/v2/events/list"),
                eq(Query.of("begin_raised_at", "2025-11-01T18:06:12Z")
                        .add("end_raised_at", "2025-11-05T18:06:12Z")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<Event> events = EventsListOperation.call(transport, tokenSupplier, Instant.parse("2025-11-01T18:06:12Z"),
                Instant.parse("2025-11-05T18:06:12Z"),
                EventsQuery.empty());

        assertThat(events).containsExactly(EventDataFixtures.fullEvent());
    }

    private static List<Event> expectedEvents() {
        final String ua2560 = "Ростелеком Ключ/128668 CFNetwork/3860.700.2 Darwin/25.6.0";
        final String ua2530 = "Ростелеком Ключ/128668 CFNetwork/3860.400.51 Darwin/25.3.0";
        final String uaJava = "Java-http-client/21.0.11";
        final String reqA5 = "a5fe8252-64cd-4ad2-84f6-1a5d8ed314b6";
        final String req6a = "6a2be6fe-8f2f-46fc-9af2-d4ad20296786";
        final String reqF0 = "f0c50e72-e1f3-4195-99be-c3b5bd72884c";
        final String push77 = "77774852A4C39B7777B081C5555A5FA97B0AD71016F57574166F41356D60A99D";
        final String push99 = "999999F3890D26FF6BCC3F36D4FD9D68D8DEBF939EBE0EF2";
        final String push98 = "9999F3890D26FF6BCC3F36D4FDED8D68D8DEBF939EBE0EF2";

        return List.of(
                openRemote("2025-10-05T20:18:04Z", ua2560, reqA5),
                openRemote("2025-10-05T19:21:46Z", ua2530, req6a),
                openRemote("2025-10-05T16:14:34Z", ua2530, reqF0),
                openRemote("2025-10-05T15:21:28Z", ua2530, reqF0),
                openRemote("2025-10-05T12:01:01Z", ua2530, reqF0),
                openRemote("2025-10-05T12:00:59Z", ua2530, reqF0),
                openRemote("2025-10-05T12:00:54Z", ua2530, reqF0),
                openRemote("2025-10-05T12:00:48Z", ua2530, reqF0),
                userEntry("2025-10-05T12:00:42Z", ua2530, "ios", push77, reqF0),
                userEntry("2025-10-05T12:00:42Z", ua2530, "ios", push77, reqF0),
                openRemote("2025-10-05T11:52:28Z", ua2560, reqF0),
                openRemote("2025-10-05T05:08:19Z", ua2560, reqF0),
                userEntry("2025-10-05T05:08:16Z", ua2560, "ios", push99, reqF0),
                userEntry("2025-10-05T01:45:08Z", uaJava, null, null, reqF0),
                userEntry("2025-10-04T23:13:18Z", uaJava, null, null, reqF0),
                userEntry("2025-10-04T02:59:01Z", ua2560, "ios", push98, reqF0),
                openRemote("2025-10-03T23:55:33Z", ua2530, reqF0),
                openRemote("2025-10-03T23:55:28Z", ua2530, reqF0),
                openRemote("2025-10-03T23:55:26Z", ua2530, reqF0),
                openRemote("2025-10-03T18:07:51Z", ua2560, reqF0)
        );
    }

    private static Event openRemote(String raisedAt, String userAgent, String requestId) {

        return new Event(
                Instant.parse(raisedAt), "api_open_remote", 55119, "intercom", 9944, 77445566,
                null, 9911005, userAgent, 5555, 555111444L, "555",
                null, null, null, null, requestId, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null);
    }

    private static Event userEntry(String raisedAt, String userAgent, @Nullable String os,
                                   @Nullable String pushToken, String requestId) {

        return new Event(
                Instant.parse(raisedAt), "user_entry", null, null, null, null,
                null, 9911005, userAgent, null, null, null,
                null, null, null, null, requestId, null, null, null, null, null, null, os, pushToken,
                null, null, null, null, null, null, null, null, null);
    }
}
