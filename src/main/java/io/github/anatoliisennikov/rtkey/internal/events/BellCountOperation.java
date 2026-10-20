package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.BellCountQuery;
import io.github.anatoliisennikov.rtkey.model.NotificationStatus;

import java.util.function.Supplier;

final class BellCountOperation {
    private static final String PATH = "/bell/api/v1/notification/count";

    private BellCountOperation() {
    }

    public static int call(HttpTransport transport, Supplier<String> tokenSupplier, BellCountQuery query) {
        Response response = transport.get(Host.KEYAPIS, PATH, toQuery(query), tokenSupplier);
        return SnakeCaseDecoder.data(response, Integer.class);
    }

    private static Query toQuery(BellCountQuery query) {
        Query result = Query.empty();
        for (String orpon : query.orpons()) {
            result = result.add("filter.orpons", orpon);
        }
        for (String roomNumber : query.roomNumbers()) {
            result = result.add("filter.roomNumbers", roomNumber);
        }
        for (NotificationStatus status : query.statusTypes()) {
            result = result.add("filter.statusTypes", status.wire());
        }
        return result;
    }
}
