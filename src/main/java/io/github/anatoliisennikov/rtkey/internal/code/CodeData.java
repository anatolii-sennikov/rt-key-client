package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.model.Code;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

record CodeData(
        Integer id,
        String code,
        String type,
        @Nullable
        String title,
        @Nullable
        Flat flat,
        @Nullable
        Company company,
        String ownerType,
        @Nullable
        Owner owner,
        String creatorType,
        @Nullable
        Creator creator,
        Instant createdAt,
        @Nullable
        Instant beginAt,
        @Nullable
        Instant expiresAt,
        List<CodeStatus> statuses
) {
    CodeData {
        Objects.requireNonNull(statuses, "statuses is required");
    }

    record Flat(Integer id, String number) {
    }

    record Company(Integer id) {
    }

    record Owner(
            Integer id,
            @Nullable
            String email,
            String login,
            String phoneNumber,
            String status
    ) {
    }

    record Creator(
            Integer id,
            String email,
            String login,
            String phoneNumber,
            String status
    ) {
    }

    static Code toModel(CodeData data){
        return new Code(
                data.id(),
                data.code(),
                data.type(),
                data.title(),
                data.flat() != null ? data.flat().id() : null,
                data.company() != null ? data.company().id() : null,
                data.ownerType(),
                data.owner() != null ? data.owner().id() : null,
                data.owner() != null ? data.owner().login() : null,
                data.owner() != null ? data.owner().phoneNumber() : null,
                data.owner() != null ? data.owner().status() : null,
                data.creator() != null ? data.creator().id() : null,
                data.creator() != null ? data.creator().login() : null,
                data.creator() != null ? data.creator().phoneNumber() : null,
                data.creator() != null ? data.creator().status() : null,
                data.statuses().stream().map(status -> new Code.Status(
                        status.device().id(),
                        status.device().company().id(),
                        status.device().title(),
                        status.status()
                )).toList(),
                data.createdAt(),
                data.expiresAt()
        );
    }
}
