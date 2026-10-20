package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.model.Event;

import java.time.Instant;

public final class EventDataFixtures {
    public static final Instant RAISED_AT = Instant.parse("2025-05-06T07:08:09Z");

    private EventDataFixtures() {
    }

    public static EventData fullEventData() {
        return new EventData(
                RAISED_AT,
                "door_open",
                101,
                "intercom",
                202,
                303,
                4,
                404,
                "rt-key-test-agent/1.0",
                505,
                606L,
                "77",
                "+74951110001",
                "+74951110002",
                "1234",
                "0011223344AA",
                "req-777",
                "+74951110003",
                "tenant",
                "user-login",
                "user-password",
                "10.0.0.7",
                "Sputnik-3",
                "Android 14",
                "push-token-abc",
                808,
                909,
                "777bcaa6-2222-4444-8888-5b92bc6808e5",
                "https://media.example/screenshot.jpg",
                "А123ВС77",
                "sedan",
                "Курьер Сервис",
                "ORD-42",
                "courier-13"
        );
    }

    public static Event fullEvent() {
        return new Event(
                RAISED_AT,
                "door_open",
                101,
                "intercom",
                202,
                303,
                4,
                404,
                "rt-key-test-agent/1.0",
                505,
                606L,
                "77",
                "+74951110001",
                "+74951110002",
                "1234",
                "0011223344AA",
                "req-777",
                "+74951110003",
                "tenant",
                "user-login",
                "user-password",
                "10.0.0.7",
                "Sputnik-3",
                "Android 14",
                "push-token-abc",
                808,
                909,
                "777bcaa6-2222-4444-8888-5b92bc6808e5",
                "https://media.example/screenshot.jpg",
                "А123ВС77",
                "sedan",
                "Курьер Сервис",
                "ORD-42",
                "courier-13"
        );
    }
}
