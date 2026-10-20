package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.CameraService;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.ArchiveInterval;
import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public final class CameraServiceImpl implements CameraService {
    private final HttpTransport transport;
    private final Supplier<String> tokenSupplier;

    public CameraServiceImpl(HttpTransport transport, Supplier<String> tokenSupplier) {
        this.transport = Objects.requireNonNull(transport, "transport");
        this.tokenSupplier = Objects.requireNonNull(tokenSupplier, "tokenSupplier");
    }

    @Override
    public List<LocalDate> archiveCalendar(UUID cameraUid) {
        Objects.requireNonNull(cameraUid, "cameraUid must not be null");
        return ArchiveCalendarOperation.call(transport, tokenSupplier, cameraUid);
    }

    @Override
    public List<ArchiveInterval> archiveIntervals(UUID cameraUid) {
        Objects.requireNonNull(cameraUid, "cameraUid must not be null");
        return ArchiveIntervalsOperation.call(transport, tokenSupplier, cameraUid);
    }

    @Override
    public List<CameraData> cameras() {
        return CamerasListOperation.call(transport, tokenSupplier, LimitOffsetQuery.empty());
    }

    @Override
    public List<CameraData> cameras(LimitOffsetQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        return CamerasListOperation.call(transport, tokenSupplier, query);
    }
}
