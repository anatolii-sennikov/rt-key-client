package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.model.Event;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class EventDataTest {
    private static final Instant RAISED_AT = EventDataFixtures.RAISED_AT;

    @Test
    public void toModelMapsEveryPopulatedField() {
        Event event = EventData.toModel(EventDataFixtures.fullEventData());

        assertThat(event).isEqualTo(EventDataFixtures.fullEvent());
    }

    @Test
    public void toModelKeepsEveryNullableFieldAbsent() {
        Event event = EventData.toModel(minimalEventData());

        assertThat(event).isEqualTo(minimalEvent());
    }

    @Test
    public void toModelKeepsNonNullableFieldsOfMinimalEvent() {
        Event event = EventData.toModel(minimalEventData());

        assertThat(event.raisedAt()).isEqualTo(RAISED_AT);
        assertThat(event.eventType()).isEqualTo("door_open");
        assertThat(event.userId()).isEqualTo(404);
        assertThat(event.userAgent()).isEqualTo("rt-key-test-agent/1.0");
        assertThat(event.requestId()).isEqualTo("req-777");
    }

    private static EventData minimalEventData() {
        return new EventData(
                RAISED_AT,
                "door_open",
                null,
                null,
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
                null,
                null,
                "req-777",
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
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private static Event minimalEvent() {
        return new Event(
                RAISED_AT,
                "door_open",
                null,
                null,
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
                null,
                null,
                "req-777",
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
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
