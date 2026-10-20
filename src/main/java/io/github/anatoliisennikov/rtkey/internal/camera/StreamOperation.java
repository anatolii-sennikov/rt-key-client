package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.CameraStream;
import io.github.anatoliisennikov.rtkey.exception.RtKeyContractException;
import io.github.anatoliisennikov.rtkey.internal.transport.Headers;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.CameraData;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

final class StreamOperation {

    private static final String TEMPLATE_STREAM_PATH = "/stream/{CAMERA_UID}/live.mp4";
    private static final String TEMPLATE_ARCHIVE_PATH = "/stream/{CAMERA_UID}/{UNIX}.mp4";
    private static final String TEMPLATE_QUERY = "mp4-fragment-length=0.5&mp4-use-speed=0&mp4-afiller=1&token={RTKEY_STREAM_TOKEN}";

    private StreamOperation() {

    }

    static CameraStream live(HttpTransport transport, CameraData camera) {
        return open(transport, camera, TEMPLATE_STREAM_PATH
                .replace("{CAMERA_UID}", camera.uid().toString()));
    }

    static CameraStream archive(HttpTransport transport, CameraData camera, Instant from) {
        return open(transport, camera, TEMPLATE_ARCHIVE_PATH
                .replace("{CAMERA_UID}", camera.uid().toString())
                .replace("{UNIX}", String.valueOf(from.getEpochSecond())));
    }

    private static CameraStream open(HttpTransport transport, CameraData camera, String path) {
        URI streamerUrl = URI.create(camera.streamerUrl());
        String query = TEMPLATE_QUERY.replace("{RTKEY_STREAM_TOKEN}", camera.streamerToken());
        URI uri;
        try {
            uri = new URI(streamerUrl.getScheme(), streamerUrl.getAuthority(), path, query, null);
        } catch (URISyntaxException e) {
            throw new RtKeyContractException("Failed to build stream URI for camera " + camera.uid(), e);
        }
        return new CameraStream(transport.openStream(uri, Headers.empty()));
    }
}
