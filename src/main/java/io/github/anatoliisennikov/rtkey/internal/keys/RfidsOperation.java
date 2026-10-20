package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.EmptyDecoder;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;
import io.github.anatoliisennikov.rtkey.model.Page;
import io.github.anatoliisennikov.rtkey.model.Rfid;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.function.Supplier;

final class RfidsOperation {
    private static final String PATH = "/api/v5/devices/rfids";

    private RfidsOperation() {

    }

    public static Page<Rfid> call(HttpTransport transport, Supplier<String> tokenSupplier, LimitOffsetQuery query) {
        Response response = transport.get(Host.HOUSEHOLD, PATH, toQuery(query), tokenSupplier);
        Page<RfidData> page = SnakeCaseDecoder.data(response, new TypeReference<>() {
        });
        List<Rfid> rfids = page.items().stream().map(RfidData::toModel).toList();
        return new Page<>(rfids, page.total());
    }

    public static void deleteRfid(HttpTransport transport, Supplier<String> tokenSupplier, Integer rfidId) {
        String path = PATH + "/" + rfidId;
        Response response = transport.delete(Host.HOUSEHOLD, path, tokenSupplier);
        EmptyDecoder.decode(response);
    }

    private static Query toQuery(LimitOffsetQuery query) {
        Query result = Query.empty();
        if (query.limit() != null) {
            result = result.add("limit", String.valueOf(query.limit()));
        }
        if (query.offset() != null) {
            result = result.add("offset", String.valueOf(query.offset()));
        }
        return result;
    }
}
