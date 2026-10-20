package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.model.CameraData;

import java.time.Instant;
import java.util.UUID;


public final class CameraDataFixtures {
    public static final UUID CAMERA_UID = UUID.fromString("777bcaa6-2222-4444-8888-5b92bc6808e5");
    public static final String STREAMER_URL = "https://live-vdk4.camera.rt.ru/blue7";
    public static final String STREAMER_TOKEN = "stream.token.secret";
    public static final String USER_TOKEN = "token.user.";
    public static final String SCREENSHOT_TOKEN = "screen.token.secrtet";
    public static final String SCREENSHOT_URL_TEMPLATE =
            "https://media-vdk4.camera.rt.ru/image/{size}/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/{timestamp}.jpg?token={cdn_token}";
    public static final String SCREENSHOT_PRECISE_URL_TEMPLATE =
            "https://media-vdk4.camera.rt.ru/image/precise/{size}/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/{timestamp}.jpg?token={cdn_token}";

    private CameraDataFixtures() {
    }

    public static CameraData camera() {
        return camera(SCREENSHOT_URL_TEMPLATE, SCREENSHOT_PRECISE_URL_TEMPLATE, STREAMER_URL);
    }

    public static CameraData camera(String screenshotUrlTemplate, String screenshotPreciseUrlTemplate,
                                    String streamerUrl) {
        return new CameraData(
                CAMERA_UID,
                "Джеймс05/007",
                streamerUrl,
                "online",
                "Доступна",
                screenshotUrlTemplate,
                screenshotPreciseUrlTemplate,
                USER_TOKEN,
                STREAMER_TOKEN,
                SCREENSHOT_TOKEN,
                10080,
                null,
                Instant.parse("2023-02-13T14:19:48Z"),
                Instant.parse("2025-01-26T11:44:13Z"),
                180
        );
    }
}
