package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.CameraMediaService;
import io.github.anatoliisennikov.rtkey.CameraStream;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.ScreenshotSize;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

public final class CameraMediaServiceImpl implements CameraMediaService {
    private final HttpTransport httpTransport;

    public CameraMediaServiceImpl(HttpTransport binaryTransport) {
        this.httpTransport = Objects.requireNonNull(binaryTransport, "binaryTransport");
    }

    @Override
    public byte[] screenshot(CameraData camera, ScreenshotSize size, @Nullable Instant at) {
        Objects.requireNonNull(camera, "camera must not be null");
        Objects.requireNonNull(size, "size must not be null");
        return ScreenshotOperation.call(httpTransport, camera, size, at);
    }

    @Override
    public byte[] screenshotPrecise(CameraData camera, ScreenshotSize size, @Nullable Instant at) {
        Objects.requireNonNull(camera, "camera must not be null");
        Objects.requireNonNull(size, "size must not be null");
        return ScreenshotOperation.callPrecise(httpTransport, camera, size, at);
    }

    @Override
    public CameraStream live(CameraData camera) {
        Objects.requireNonNull(camera, "camera must not be null");
        return StreamOperation.live(httpTransport, camera);
    }

    @Override
    public CameraStream archive(CameraData camera, Instant from) {
        Objects.requireNonNull(camera, "camera must not be null");
        Objects.requireNonNull(from, "from must not be null");
        return StreamOperation.archive(httpTransport, camera, from);
    }
}
