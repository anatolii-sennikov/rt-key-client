package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyContractException;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.ArchiveInterval;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

final class ArchiveIntervalsOperation {
    private static final String PATH = "/api/v1/cameras/{uid}/archive_intervals";

    private ArchiveIntervalsOperation() {
    }

    public static List<ArchiveInterval> call(HttpTransport transport, Supplier<String> tokenSupplier, UUID cameraUid) {
        String path = PATH.replace("{uid}", cameraUid.toString());

        Response response = transport.get(Host.VC, path, tokenSupplier);
        ArchiveIntervalsData data = SnakeCaseDecoder.data(response, ArchiveIntervalsData.class);
        return data.items().stream().map(ArchiveIntervalsOperation::toInterval).toList();
    }

    private static ArchiveInterval toInterval(BigDecimal[] pair) {
        if (pair.length != 2) {
            throw new RtKeyContractException("archive_intervals: interval must have 2 elements, got " + pair.length);
        }
        return new ArchiveInterval(toInstant(pair[0]), toInstant(pair[1]));
    }

    private static Instant toInstant(BigDecimal epochSeconds) {
        BigDecimal[] parts = epochSeconds.divideAndRemainder(BigDecimal.ONE);
        return Instant.ofEpochSecond(parts[0].longValueExact(),
                parts[1].movePointRight(9).longValueExact());
    }
}
