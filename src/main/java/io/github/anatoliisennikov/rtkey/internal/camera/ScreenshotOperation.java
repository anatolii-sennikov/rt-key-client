package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.internal.transport.Headers;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.ScreenshotSize;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.time.Instant;

final class ScreenshotOperation {
    private ScreenshotOperation() {

    }

    static byte[] call(HttpTransport transport, CameraData camera, ScreenshotSize size, @Nullable Instant at) {
        return execute(transport, camera, camera.screenshotUrlTemplate(), size, at);
    }

    static byte[] callPrecise(HttpTransport transport, CameraData camera, ScreenshotSize size, @Nullable Instant at) {
        return execute(transport, camera, camera.screenshotPreciseUrlTemplate(), size, at);
    }

    private static byte[] execute(HttpTransport transport, CameraData camera, String urlTemplate, ScreenshotSize size, @Nullable Instant at) {

        String timestamp = at == null ? "last" : String.valueOf(at.getEpochSecond());
        Headers cookie = Headers.of("Cookie", "utoken=" + camera.userToken());
        String url = urlTemplate
                .replace("{size}", size.wire())
                .replace("{timestamp}", timestamp)
                .replace("{cdn_token}", camera.screenshotToken())
                .replace("{uid}", camera.uid().toString());

        return transport.getBytes(URI.create(url), cookie);
    }
}
