package io.github.anatoliisennikov.rtkey.internal.code;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

record StatusesDevice(
        Integer id,
        DeviceCompany company,
        String type,
        String title,
        @Nullable
        String fullCode,
        @Nullable
        String callNumber
) {
        StatusesDevice{
                Objects.requireNonNull(company, "company is required");
        }
}
