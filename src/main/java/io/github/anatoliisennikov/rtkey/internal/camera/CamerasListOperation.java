package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.CamelCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.function.Supplier;

final class CamerasListOperation {
    private static final String PATH = "/vc/api/v1/camera_video_data/list";

    private CamerasListOperation() {

    }

    public static List<CameraData> call(HttpTransport transport, Supplier<String> tokenSupplier, LimitOffsetQuery query) {
        Response response = transport.get(Host.KEYAPIS, PATH, toQuery(query), tokenSupplier);
        List<CameraWireData> data = CamelCaseDecoder.data(response, new TypeReference<>() {
        });

        return data.stream().map(CameraWireData::toModel).toList();
    }

    private static Query toQuery(LimitOffsetQuery query) {
        Query result = Query.empty();
        if (query.limit() != null) {
            result = result.add("paging.limit", String.valueOf(query.limit()));
        }
        if (query.offset() != null) {
            result = result.add("paging.offset", String.valueOf(query.offset()));
        }
        return result;
    }
}
