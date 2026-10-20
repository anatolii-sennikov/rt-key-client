package io.github.anatoliisennikov.rtkey.internal.account;

import org.jspecify.annotations.Nullable;

import java.util.List;

record Flat(
        Integer id,
        Integer companyId,
        String name,
        Integer area,
        @Nullable
        String account,
        List<Object> sips,
        List<Object> tfopPhones,
        UserSettings userSettings,
        String type,
        RoomPermissions roomPermissions,
        List<String> actionsPermissions,
        Boolean isSubscriptionEnable
) {
}
