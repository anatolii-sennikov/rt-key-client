package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.ArchiveInterval;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class ArchiveIntervalsOperationTest extends BaseOperationTest {

    @Test
    public void archiveIntervals() throws IOException {
        final String data = getResourceAsString("/camera/archive_intervals.json");
        UUID cameraUid = UUID.randomUUID();

        String path = "/api/v1/cameras/{uid}/archive_intervals".replace("{uid}", cameraUid.toString());

        when(transport.get(eq(Host.VC), eq(path), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<ArchiveInterval> result = ArchiveIntervalsOperation.call(transport, tokenSupplier, cameraUid);

        assertThat(result.size()).isEqualTo(58);
        assertThat(result.getFirst()).isEqualTo(new ArchiveInterval(
                Instant.ofEpochSecond(1791604022L, 0L),
                Instant.ofEpochSecond(1791605940L, 267922200L)
        ));

        assertThat(result.get(57)).isEqualTo(new ArchiveInterval(
                Instant.ofEpochSecond(1792179397L, 713044400L),
                Instant.ofEpochSecond(1792208820L, 25044400L)
        ));
    }

    @Test
    public void archiveIntervalsCameraNotFound() throws IOException {
        final String data = getResourceAsString("/camera/archive_intervals_not_found_camera.json");
        UUID cameraUid = UUID.randomUUID();

        String path = "/api/v1/cameras/{uid}/archive_intervals".replace("{uid}", cameraUid.toString());

        when(transport.get(eq(Host.VC), eq(path), same(tokenSupplier)))
                .thenReturn(new Response(404, data));

        assertThatThrownBy(() -> ArchiveIntervalsOperation.call(transport, tokenSupplier, cameraUid))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Объект не найден: camera_id");
    }
}
