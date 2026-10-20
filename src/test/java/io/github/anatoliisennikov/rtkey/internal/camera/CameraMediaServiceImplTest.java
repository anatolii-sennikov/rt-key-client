package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.BaseServiceTest;
import io.github.anatoliisennikov.rtkey.internal.transport.Headers;
import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.ScreenshotSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.URI;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class CameraMediaServiceImplTest extends BaseServiceTest {
    private static final String SMALL_LAST_JPG = "/small/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/last.jpg";
    private static final Headers SCREENSHOT_COOKIE =
            Headers.of("Cookie", "utoken=" + CameraDataFixtures.USER_TOKEN);

    private final CameraData camera = CameraDataFixtures.camera();

    private CameraMediaServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new CameraMediaServiceImpl(transport);
    }

    @Test
    public void screenshotBindsScreenshotUrlTemplate() {
        assertThat(service.screenshot(camera, ScreenshotSize.SMALL, null)).isNull();

        assertThat(capturedScreenshotUri().getPath()).isEqualTo("/image" + SMALL_LAST_JPG);
    }

    @Test
    public void screenshotPreciseBindsPreciseUrlTemplate() {
        assertThat(service.screenshotPrecise(camera, ScreenshotSize.SMALL, null)).isNull();

        assertThat(capturedScreenshotUri().getPath()).isEqualTo("/image/precise" + SMALL_LAST_JPG);
    }

    @Test
    public void liveBindsLiveStreamPath() {
        assertReachedTransport(() -> service.live(camera));

        assertThat(capturedStreamUri().getPath())
                .isEqualTo("/stream/" + CameraDataFixtures.CAMERA_UID + "/live.mp4");
    }

    @Test
    public void archiveBindsTimestampedStreamPath() {
        Instant from = Instant.ofEpochSecond(1700000000L);

        assertReachedTransport(() -> service.archive(camera, from));

        assertThat(capturedStreamUri().getPath())
                .isEqualTo("/stream/" + CameraDataFixtures.CAMERA_UID + "/1700000000.mp4");
    }

    private URI capturedScreenshotUri() {
        ArgumentCaptor<URI> uri = ArgumentCaptor.forClass(URI.class);

        verify(transport).getBytes(uri.capture(), eq(SCREENSHOT_COOKIE));

        return uri.getValue();
    }

    private URI capturedStreamUri() {
        ArgumentCaptor<URI> uri = ArgumentCaptor.forClass(URI.class);

        verify(transport).openStream(uri.capture(), eq(Headers.empty()));

        return uri.getValue();
    }
}
