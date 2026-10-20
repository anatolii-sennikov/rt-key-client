package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

final class ArchiveCalendarOperation {
    private static final String PATH = "/api/v1/cameras/{uid}/archive_calendar";

    private ArchiveCalendarOperation() {

    }

    public static List<LocalDate> call(HttpTransport transport, Supplier<String> tokenSupplier, UUID cameraUid) {
        String path = PATH.replace("{uid}", cameraUid.toString());

        Response response = transport.get(Host.VC, path, tokenSupplier);

        ArchiveCalendarData data = SnakeCaseDecoder.data(response, ArchiveCalendarData.class);
        return List.copyOf(data.items());
    }
}
