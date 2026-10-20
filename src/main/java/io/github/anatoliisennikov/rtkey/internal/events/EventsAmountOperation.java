package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.EventTypes;
import io.github.anatoliisennikov.rtkey.model.EventsQuery;

import java.time.Instant;
import java.util.function.Supplier;

final class EventsAmountOperation {
    private static final String PATH = "/api/v2/events/amount";

    private EventsAmountOperation() {
    }

    public static int call(HttpTransport transport, Supplier<String> tokenSupplier, Instant begin, Instant end,
                           EventsQuery eventsQuery) {
        Query query = toQuery(eventsQuery)
                .add("begin_raised_at", begin.toString())
                .add("end_raised_at", end.toString());

        Response response = transport.get(Host.EVENTS, PATH, query, tokenSupplier);

        AmountData data = SnakeCaseDecoder.data(response, AmountData.class);
        return data.total();
    }

    private static Query toQuery(EventsQuery query) {
        Query result = Query.empty();
        if (query.limit() != null) {
            result = result.add("limit", String.valueOf(query.limit()));
        }
        if (query.offset() != null) {
            result = result.add("offset", String.valueOf(query.offset()));
        }
        for (String deviceId : query.deviceIds()) {
            result = result.add("device_ids", deviceId);
        }
        for (EventTypes eventType : query.eventTypes()) {
            result = result.add("event_types", eventType.wire());
        }
        return result;
    }
}
