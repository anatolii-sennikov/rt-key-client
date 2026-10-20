package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.model.Event;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EventsListDataTest {
    private static final Instant RAISED_AT = Instant.parse("2025-07-21T06:05:04Z");

    @Test
    public void toModelMapsEveryItemInOrder() {
        EventsListData data = new EventsListData(List.of(
                eventData(101, "door_open", "req-1", "1111"),
                eventData(202, "call", "req-2", "2222"),
                eventData(303, "rfid_access", "req-3", "3333")
        ));

        List<Event> events = EventsListData.toModel(data);

        assertThat(events).hasSize(3);
        assertThat(events).extracting(Event::deviceId).containsExactly(101, 202, 303);
        assertThat(events).extracting(Event::eventType)
                .containsExactly("door_open", "call", "rfid_access");
        assertThat(events).extracting(Event::requestId).containsExactly("req-1", "req-2", "req-3");
        assertThat(events).extracting(Event::code).containsExactly("1111", "2222", "3333");
    }

    @Test
    public void toModelKeepsEveryFieldOfEachItem() {
        EventsListData data = new EventsListData(List.of(eventData(101, "door_open", "req-1", "1111")));

        List<Event> events = EventsListData.toModel(data);

        assertThat(events).hasSize(1);
        Event event = events.get(0);
        assertThat(event.raisedAt()).isEqualTo(RAISED_AT);
        assertThat(event.deviceType()).isEqualTo("intercom");
        assertThat(event.userId()).isEqualTo(404);
        assertThat(event.userAgent()).isEqualTo("rt-key-test-agent/1.0");
        assertThat(event.cameraUid()).isEqualTo("777bcaa6-2222-4444-8888-5b92bc6808e5");
        assertThat(event.companyId()).isNull();
        assertThat(event.courierId()).isNull();
    }

    @Test
    public void toModelKeepsEmptyItemsEmpty() {
        EventsListData data = new EventsListData(List.of());

        assertThat(EventsListData.toModel(data)).isEmpty();
    }

    private static EventData eventData(Integer deviceId, String eventType, String requestId, String code) {
        return new EventData(
                RAISED_AT,
                eventType,
                deviceId,
                "intercom",
                null,
                null,
                null,
                404,
                "rt-key-test-agent/1.0",
                null,
                null,
                null,
                null,
                null,
                code,
                null,
                requestId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "777bcaa6-2222-4444-8888-5b92bc6808e5",
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
