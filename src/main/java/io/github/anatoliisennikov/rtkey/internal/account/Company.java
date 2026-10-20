package io.github.anatoliisennikov.rtkey.internal.account;

import org.jspecify.annotations.Nullable;

record Company(
        Integer id,
        Integer vcId,
        String title,
        Integer utcOffset,
        @Nullable
        String linkTitle,
        @Nullable
        String iosDeepLink,
        @Nullable
        String iosAppStoreLink,
        @Nullable
        String androidPackageId,
        @Nullable
        String huaweiAppgalleryPackageName,
        @Nullable
        String webLink,
        @Nullable
        String linkDescription
) {
}
