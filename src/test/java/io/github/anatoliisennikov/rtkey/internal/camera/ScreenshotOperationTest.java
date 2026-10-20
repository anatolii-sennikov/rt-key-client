package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.internal.transport.Headers;
import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.ScreenshotSize;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ScreenshotOperationTest extends BaseOperationTest {
    private static final String MEDIA_BASE = "https://media-vdk4.camera.rt.ru/image";
    private static final Headers EXPECTED_COOKIE = Headers.of("Cookie", "utoken=" + CameraDataFixtures.USER_TOKEN);

    @Test
    public void screenshotLastSmall() {
        byte[] expectedBytes = new byte[]{1, 2, 3};
        URI expectedUri = URI.create(
                MEDIA_BASE + "/small/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/last.jpg?token="
                        + CameraDataFixtures.SCREENSHOT_TOKEN
        );

        when(transport.getBytes(eq(expectedUri), eq(EXPECTED_COOKIE))).thenReturn(expectedBytes);

        byte[] result = ScreenshotOperation.call(transport, CameraDataFixtures.camera(), ScreenshotSize.SMALL, null);

        assertThat(result).isEqualTo(expectedBytes);
    }

    @Test
    public void screenshotAtSpecificTime() {
        byte[] expectedBytes = new byte[]{4, 5, 6};
        Instant at = Instant.ofEpochSecond(1700000000L);
        URI expectedUri = URI.create(
                MEDIA_BASE + "/medium/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/1700000000.jpg?token="
                        + CameraDataFixtures.SCREENSHOT_TOKEN
        );

        when(transport.getBytes(eq(expectedUri), eq(EXPECTED_COOKIE))).thenReturn(expectedBytes);

        byte[] result = ScreenshotOperation.call(transport, CameraDataFixtures.camera(), ScreenshotSize.MEDIUM, at);

        assertThat(result).isEqualTo(expectedBytes);
    }

    @Test
    public void screenshotReplacesUidPlaceholder() {
        byte[] expectedBytes = new byte[]{10, 11, 12};
        String template = MEDIA_BASE + "/{size}/{uid}/{timestamp}.jpg?token={cdn_token}";
        CameraData camera = CameraDataFixtures.camera(template, template, CameraDataFixtures.STREAMER_URL);
        URI expectedUri = URI.create(
                MEDIA_BASE + "/small/" + CameraDataFixtures.CAMERA_UID + "/last.jpg?token="
                        + CameraDataFixtures.SCREENSHOT_TOKEN
        );

        when(transport.getBytes(eq(expectedUri), eq(EXPECTED_COOKIE))).thenReturn(expectedBytes);

        byte[] result = ScreenshotOperation.call(transport, camera, ScreenshotSize.SMALL, null);

        assertThat(result).isEqualTo(expectedBytes);
    }

    @Test
    public void screenshotPreciseOriginal() {
        byte[] expectedBytes = new byte[]{7, 8, 9};
        URI expectedUri = URI.create(
                MEDIA_BASE + "/precise/original/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/last.jpg?token="
                        + CameraDataFixtures.SCREENSHOT_TOKEN
        );

        when(transport.getBytes(eq(expectedUri), eq(EXPECTED_COOKIE))).thenReturn(expectedBytes);

        byte[] result = ScreenshotOperation.callPrecise(transport, CameraDataFixtures.camera(), ScreenshotSize.ORIGINAL, null);

        assertThat(result).isEqualTo(expectedBytes);
    }

    @Test
    public void screenshotPreciseAtSpecificTime() {
        byte[] expectedBytes = new byte[]{13, 14, 15};
        Instant at = Instant.ofEpochSecond(1700000000L);
        URI expectedUri = URI.create(
                MEDIA_BASE + "/precise/medium/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/1700000000.jpg?token="
                        + CameraDataFixtures.SCREENSHOT_TOKEN
        );

        when(transport.getBytes(eq(expectedUri), eq(EXPECTED_COOKIE))).thenReturn(expectedBytes);

        byte[] result = ScreenshotOperation.callPrecise(transport, CameraDataFixtures.camera(), ScreenshotSize.MEDIUM, at);

        assertThat(result).isEqualTo(expectedBytes);
    }
}
