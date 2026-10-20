package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

/**
 * A camera as returned by {@link io.github.anatoliisennikov.rtkey.CameraService#cameras()}.
 *
 * <p>Pass an instance to {@link io.github.anatoliisennikov.rtkey.CameraMediaService} to obtain a
 * live stream, an archive stream or screenshots: the media methods build their URLs from
 * {@link #streamerUrl()}, {@link #screenshotUrlTemplate()}, {@link #screenshotPreciseUrlTemplate()}
 * and the tokens.
 *
 * <p>{@link #toString()} redacts the user, streamer and screenshot tokens.
 *
 * @param uid                          camera UID
 * @param title                        camera title
 * @param streamerUrl                  streamer URL; only its origin is used when building
 *                                     stream URLs
 * @param statusType                   camera status type reported by the server
 * @param statusTitle                  human-readable camera status title reported by the server
 * @param screenshotUrlTemplate        screenshot URL template; the placeholders {@code {size}},
 *                                     {@code {timestamp}}, {@code {cdn_token}} and {@code {uid}}
 *                                     are filled by {@code CameraMediaService}
 * @param screenshotPreciseUrlTemplate precise screenshot URL template, used by
 *                                     {@code CameraMediaService.screenshotPrecise}; the precise
 *                                     variant uses the same size values and the resulting image
 *                                     dimensions are defined by the server
 * @param userToken                    token sent as the {@code utoken} cookie when requesting
 *                                     screenshots
 * @param streamerToken                token used in the stream query
 * @param screenshotToken              token substituted for the {@code {cdn_token}} placeholder
 * @param archiveLength                archive length reported by the server;
 *                                     in server-defined units
 * @param location                     camera location, when set
 * @param createdAt                    creation timestamp
 * @param updatedAt                    last update timestamp
 * @param utcOffset                    UTC offset reported by the server; in server-defined units
 */
public record CameraData(
        UUID uid,
        String title,
        String streamerUrl,
        @Nullable
        String statusType,
        @Nullable
        String statusTitle,
        String screenshotUrlTemplate,
        String screenshotPreciseUrlTemplate,
        String userToken,
        String streamerToken,
        String screenshotToken,
        Integer archiveLength,
        @Nullable
        Location location,
        Instant createdAt,
        Instant updatedAt,
        Integer utcOffset
) {
    /**
     * Geographic coordinates of the camera.
     *
     * @param lat latitude
     * @param lng longitude
     */
    public record Location(double lat, double lng) {
    }

    @Override
    public String toString() {
        return "CameraData{" +
                "uid=" + uid +
                ", title='" + title + '\'' +
                ", streamerUrl='" + streamerUrl + '\'' +
                ", statusType='" + statusType + '\'' +
                ", statusTitle='" + statusTitle + '\'' +
                ", screenshotUrlTemplate='" + screenshotUrlTemplate + '\'' +
                ", screenshotPreciseUrlTemplate='" + screenshotPreciseUrlTemplate + '\'' +
                ", userToken='user token is hidden by security policy'" +
                ", streamerToken='streamer token is hidden by security policy'" +
                ", screenshotToken='screenshot token is hidden by security policy'" +
                ", archiveLength=" + archiveLength +
                ", location=" + location +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", utcOffset=" + utcOffset +
                '}';
    }
}
