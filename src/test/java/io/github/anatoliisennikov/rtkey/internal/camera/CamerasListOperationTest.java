package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class CamerasListOperationTest extends BaseOperationTest {
    private static final String PATH = "/vc/api/v1/camera_video_data/list";

    @Test
    public void camerasListEmptyQuery() throws IOException {
        final String data = getResourceAsString("/camera/camera_video_data_list.json");

        when(transport.get(eq(Host.KEYAPIS), eq(PATH), eq(Query.empty()), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<CameraData> result = CamerasListOperation.call(transport, tokenSupplier, LimitOffsetQuery.empty());

        assertThat(result).isEqualTo(List.of(CameraDataFixtures.camera()));
    }

    @Test
    public void camerasListWithPaging() throws IOException {
        final String data = getResourceAsString("/camera/camera_video_data_list.json");

        Query pagingQuery = Query.empty()
                .add("paging.limit", "10")
                .add("paging.offset", "5");

        when(transport.get(eq(Host.KEYAPIS), eq(PATH), eq(pagingQuery), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        LimitOffsetQuery query = LimitOffsetQuery.builder()
                .limit(10)
                .offset(5)
                .build();

        List<CameraData> result = CamerasListOperation.call(transport, tokenSupplier, query);

        assertThat(result).isEqualTo(List.of(CameraDataFixtures.camera()));
    }
}
