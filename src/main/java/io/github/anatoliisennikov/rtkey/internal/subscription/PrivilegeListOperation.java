package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.CamelCaseDecoder;

import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Privilege;
import io.github.anatoliisennikov.rtkey.model.PrivilegeQuery;

import java.util.List;
import java.util.function.Supplier;

final class PrivilegeListOperation {
    private static final String PATH = "/subscription/api/v1/privilege/list";

    private PrivilegeListOperation() {
    }

    public static List<Privilege> call(HttpTransport transport, Supplier<String> tokenSupplier, PrivilegeQuery query) {
        Response response = transport.get(Host.KEYAPIS, PATH, toQuery(query), tokenSupplier);
        List<PrivilegeData> data = CamelCaseDecoder.resultItems(response, PrivilegeData.class);

        return data.stream().map(PrivilegeData::toModel).toList();
    }

    private static Query toQuery(PrivilegeQuery query) {
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
