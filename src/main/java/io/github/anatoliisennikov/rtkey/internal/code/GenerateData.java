package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.model.GeneratedCode;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;

record GenerateData(
        Integer id,
        String codeType,
        String code,
        @Nullable
        String title,
        Instant expiresAt,
        Integer roomId,
        List<Integer> deviceIds,
        Integer ownerUserId,
        Instant createdAt
) {
    static GeneratedCode toModel(GenerateData data) {
        return new GeneratedCode(
                data.id(), data.codeType(), data.code(), data.title(), data.expiresAt(), data.roomId(),
                data.deviceIds(), data.ownerUserId(), data.createdAt()
        );
    }
}
