package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.model.Event;

import java.util.List;
import java.util.Objects;

record EventsListData(List<EventData> items) {
    EventsListData {
        Objects.requireNonNull(items, "items is required");
    }

    static List<Event> toModel(EventsListData data) {
        return data.items().stream().map(EventData::toModel).toList();
    }
}
