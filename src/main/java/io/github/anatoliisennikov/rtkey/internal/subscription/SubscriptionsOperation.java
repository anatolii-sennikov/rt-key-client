package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.CamelCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Subscription;
import io.github.anatoliisennikov.rtkey.model.SubscriptionQuery;

import java.util.List;
import java.util.function.Supplier;

final class SubscriptionsOperation {
    private static final String PATH = "/subscription/api/v1/subscription/list";

    private SubscriptionsOperation() {
    }

    public static List<Subscription> call(HttpTransport transport, Supplier<String> tokenSupplier,
                                          SubscriptionQuery query) {
        Response response = transport.get(Host.KEYAPIS, PATH, toQuery(query), tokenSupplier);
        List<SubscriptionData> data = CamelCaseDecoder.resultItems(response, SubscriptionData.class);

        return data.stream().map(SubscriptionData::toModel).toList();
    }

    private static Query toQuery(SubscriptionQuery query) {
        Query result = Query.empty();
        if (query.limit() != null) {
            result = result.add("paging.limit", String.valueOf(query.limit()));
        }
        for (String flatNumber : query.flatNumbers()) {
            result = result.add("filter.flatNumbers", flatNumber);
        }
        for (String orpon : query.orpons()) {
            result = result.add("filter.orpons", orpon);
        }
        return result;
    }

}
