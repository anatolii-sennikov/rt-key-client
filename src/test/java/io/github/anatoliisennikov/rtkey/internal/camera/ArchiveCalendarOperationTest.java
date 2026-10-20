package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class ArchiveCalendarOperationTest extends BaseOperationTest {

    @Test
    public void archiveCalendar() throws IOException {
        final String data = getResourceAsString("/camera/archive_calendar.json");
        UUID cameraUid = UUID.randomUUID();

        String path = "/api/v1/cameras/{uid}/archive_calendar".replace("{uid}", cameraUid.toString());

        when(transport.get(eq(Host.VC), eq(path), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<LocalDate> result = ArchiveCalendarOperation.call(transport, tokenSupplier, cameraUid);

        List<LocalDate> expected = List.of(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 11),
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 13),
                LocalDate.of(2026, 10, 14),
                LocalDate.of(2026, 10, 15),
                LocalDate.of(2026, 10, 16),
                LocalDate.of(2026, 10, 17)
        );
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void archiveCalendarCameraNotFound() throws IOException {
        final String data = getResourceAsString("/camera/archive_calendar_not_found_camera.json");
        UUID cameraUid = UUID.randomUUID();

        String path = "/api/v1/cameras/{uid}/archive_calendar".replace("{uid}", cameraUid.toString());

        when(transport.get(eq(Host.VC), eq(path), same(tokenSupplier)))
                .thenReturn(new Response(404, data));

        assertThatThrownBy(() -> ArchiveCalendarOperation.call(transport, tokenSupplier, cameraUid))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Объект не найден: camera_id");
    }
}
