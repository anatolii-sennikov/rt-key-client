package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.CameraStream;
import io.github.anatoliisennikov.rtkey.internal.transport.Headers;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class StreamOperationTest extends BaseOperationTest {
    private static final String QUERY =
            "mp4-fragment-length=0.5&mp4-use-speed=0&mp4-afiller=1&token=" + CameraDataFixtures.STREAMER_TOKEN;

    private static final Headers NO_HEADERS = Headers.empty();

    @Test
    public void liveStream() {
        InputStream expectedStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        URI expectedUri = streamUri("/stream/" + CameraDataFixtures.CAMERA_UID + "/live.mp4");

        when(transport.openStream(eq(expectedUri), eq(NO_HEADERS))).thenReturn(expectedStream);

        CameraStream result = StreamOperation.live(transport, CameraDataFixtures.camera());

        assertThat(result.body()).isSameAs(expectedStream);
    }

    @Test
    public void archiveStream() {
        InputStream expectedStream = new ByteArrayInputStream(new byte[]{4, 5, 6});
        Instant from = Instant.ofEpochSecond(1700000000L);
        URI expectedUri = streamUri("/stream/" + CameraDataFixtures.CAMERA_UID + "/1700000000.mp4");

        when(transport.openStream(eq(expectedUri), eq(NO_HEADERS))).thenReturn(expectedStream);

        CameraStream result = StreamOperation.archive(transport, CameraDataFixtures.camera(), from);

        assertThat(result.body()).isSameAs(expectedStream);
    }

    private static URI streamUri(String path) {
        URI streamer = URI.create(CameraDataFixtures.STREAMER_URL);
        return URI.create(streamer.getScheme() + "://" + streamer.getAuthority() + path + "?" + QUERY);
    }
}
