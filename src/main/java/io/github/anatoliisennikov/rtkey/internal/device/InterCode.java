package io.github.anatoliisennikov.rtkey.internal.device;

import org.jspecify.annotations.Nullable;

import java.time.Instant;

record InterCode(
         Integer id,
         String code,
         @Nullable
         Instant startDate,
         @Nullable
         Instant endDate,
         String interCodeType,
         String description,
         @Nullable
         String nameByUser
 ) {
}