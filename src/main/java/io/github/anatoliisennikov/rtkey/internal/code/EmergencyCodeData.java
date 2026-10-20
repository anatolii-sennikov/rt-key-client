package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.model.EmergencyCode;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

record EmergencyCodeData(
        Integer id,
        String code,
        String type,
        @Nullable
        String title,
        @Nullable
        Flat flat,
        @Nullable
        Company company,
        @Nullable
        String ownerType,
        @Nullable
        User owner,
        String creatorType,
        @Nullable
        User creator,
        Instant createdAt,
        @Nullable
        Instant beginAt,
        Instant expiresAt,
        List<Status> statuses


) {
    EmergencyCodeData {
        Objects.requireNonNull(statuses, "statuses is required");
    }

    static EmergencyCode toModel(EmergencyCodeData data) {
        List<EmergencyCode.Status> statuses = data.statuses().stream().map(status -> new EmergencyCode.Status(
                new EmergencyCode.Device(
                        status.device().id(),
                        new EmergencyCode.Company(status.device().company().id()),
                        status.device().type(),
                        status.device().title(),
                        status.device().fullCode(),
                        status.device().callNumber()
                ),
                status.statusChangedAt(),
                status.status()
        )).toList();

        return new EmergencyCode(
                data.id(),
                data.code(),
                data.type(),
                data.title(),
                toModel(data.flat()),
                toModel(data.company()),
                data.ownerType(),
                toModel(data.owner()),
                data.creatorType(),
                toModel(data.creator()),
                data.createdAt(),
                data.beginAt(),
                data.expiresAt(),
                statuses
        );
    }

    private static EmergencyCode.@Nullable Flat toModel(@Nullable Flat flat) {
        return flat == null ? null : new EmergencyCode.Flat(flat.id(), flat.number());
    }

    private static EmergencyCode.@Nullable Company toModel(@Nullable Company company) {
        return company == null ? null : new EmergencyCode.Company(company.id());
    }

    private static EmergencyCode.@Nullable User toModel(@Nullable User user) {
        return user == null ? null : new EmergencyCode.User(
                user.id(),
                user.firstname(),
                user.patronym(),
                user.lastname(),
                user.login(),
                user.phoneNumber(),
                user.email()
        );
    }

    record Flat(Integer id, String number) {

    }

    record User(
            Integer id,
            @Nullable
            String firstname,
            @Nullable
            String patronym,
            @Nullable
            String lastname,
            @Nullable
            String login,
            @Nullable
            String phoneNumber,
            @Nullable
            String email
    ) {
    }

    record Status(
            Device device,
            Instant statusChangedAt,
            String status
    ) {
        Status {
            Objects.requireNonNull(device, "device is required");
        }
    }

    record Device(
            Integer id,
            Company company,
            String type,
            String title,
            @Nullable
            String fullCode,
            @Nullable
            String callNumber

    ) {
        Device {
            Objects.requireNonNull(company, "company is required");
        }

    }

    record Company(Integer id) {

    }
}
