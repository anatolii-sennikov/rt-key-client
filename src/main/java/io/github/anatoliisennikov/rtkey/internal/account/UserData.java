package io.github.anatoliisennikov.rtkey.internal.account;

import io.github.anatoliisennikov.rtkey.model.User;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

record UserData(
        Integer id,
        String type,
        String login,
        String phoneNumber,
        @Nullable
        String email,
        @Nullable
        String invoiceEmail,
        String status,
        Boolean isNew,
        String ssoId,
        @Nullable
        String mosSsoId,
        Integer ssoUserId,
        Boolean ssoLinked,
        String oryId,
        Integer vcId,
        Integer ustoreId,
        Integer mrfId,
        Integer rfId,
        Company company,
        @Nullable
        String description,
        Integer creatorUserId,
        String createdBy,
        Boolean phoneNumberVerified,
        Boolean emailVerified,
        Instant createdAt,
        @Nullable
        Instant archivedAt,
        Instant updatedAt,
        @Nullable
        Instant activatedAt,
        List<Building> buildings,
        @Nullable
        String envUrl,
        @Nullable
        String ownerUserId,
        @Nullable
        String scope
) {
    UserData {
        Objects.requireNonNull(company, "company is required");
        Objects.requireNonNull(buildings, "buildings is required");
    }

    static User toModel(UserData data) {
        List<io.github.anatoliisennikov.rtkey.model.Flat> flats = data.buildings().stream()
                .flatMap(b -> b.entrances().stream()
                        .flatMap(e -> e.floors().stream()
                                .flatMap(f -> f.flats().stream()
                                        .map(flat -> new io.github.anatoliisennikov.rtkey.model.Flat(
                                                flat.id(), flat.name(),
                                                b.id(), b.orponId(), b.place(), b.street(),
                                                e.id(), e.name(),
                                                f.id(), f.name())))))
                .toList();

        return new User(data.id(), data.login(), data.phoneNumber(), data.status(),
                data.phoneNumberVerified(), data.emailVerified(),
                new User.Company(data.company().id(), data.company().title()),
                flats);
    }
}
