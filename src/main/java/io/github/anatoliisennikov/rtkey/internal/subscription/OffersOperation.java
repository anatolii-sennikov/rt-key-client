package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.CamelCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Offer;
import io.github.anatoliisennikov.rtkey.model.OfferQuery;
import io.github.anatoliisennikov.rtkey.model.OfferType;

import java.util.List;
import java.util.function.Supplier;

final class OffersOperation {
    private static final String PATH = "/subscription/api/v1/offer/list";

    private OffersOperation() {
    }

    public static List<Offer> call(HttpTransport transport, Supplier<String> tokenSupplier, OfferQuery query) {
        Response response = transport.get(Host.KEYAPIS, PATH, toQuery(query), tokenSupplier);
        List<OfferData> data = CamelCaseDecoder.resultItems(response, OfferData.class);

        return data.stream().map(OfferData::toModel).toList();
    }

    private static Query toQuery(OfferQuery query) {
        Query result = Query.empty();
        if (query.limit() != null) {
            result = result.add("paging.limit", String.valueOf(query.limit()));
        }
        for (String orpon : query.orpons()) {
            result = result.add("filter.orpons", orpon);
        }

        for (OfferType type : query.types()) {
            result = result.add("filter.types", type.wire());
        }

        return result;
    }
}
