package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.model.Device;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

record DeviceV4(
        Integer id,
        Integer companyId,
        List<Integer> accessCompanyIds,
        List<Integer> accessBuildingIds,
        List<Integer> flatgrammIds,
        String deviceType,
        String modelType,
        String serialNumber,
        String mac,
        String title,
        String host,
        String statusType,
        String swVersion,
        String hwVersion,
        Boolean kkm,
        DeviceSettings settings,
        String syncStatusType,
        Instant syncAt,
        @Nullable
        String syncError,
        Integer syncRetryCount,
        Instant successfulSyncAt,
        @Nullable
        Boolean isAutochangePasswordEnabled,
        List<String> tags,
        @Nullable
        List<String> deviceUserTypes,
        TrunkAccount trunkAccount,
        Redirect redirect,
        Boolean isDispatched,
        @Nullable
        String publicIdentity,
        UUID cameraUid,
        String cameraStatus,
        @Nullable
        String cameraError
) {
    static Device toModel(DeviceV4 data) {
        return new Device(
                data.id(),
                data.companyId(),
                data.deviceType(),
                data.modelType(),
                data.title(),
                data.statusType(),
                data.syncStatusType(),
                data.syncAt(),
                data.syncError(),
                data.syncRetryCount(),
                data.cameraUid(),
                data.cameraStatus(),
                data.cameraError()
        );
    }
}
