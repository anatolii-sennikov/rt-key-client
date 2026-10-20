package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.model.Intercom;
import org.jspecify.annotations.Nullable;

import java.util.List;

record IntercomData(
        String id,
        String deviceType,
        String serialNumber,
        @Nullable
        List<String> deviceGroup,
        Integer utcOffsetMinutes,
        @Nullable
        String cameraId,
        @Nullable
        String description,
        Boolean isFavorite,
        Boolean isActive,
        String nameByCompany,
        @Nullable
        String nameByUser,
        @Nullable
        Boolean acceptConciergeCall,
        @Nullable
        List<Capability> capabilities,
        List<InterCode> interCodes
) {
    record Capability(
            String name,
            Boolean setup
    ) {
    }

    static Intercom toModel(IntercomData data) {
        return new Intercom(
                data.id(),
                data.serialNumber(),
                data.deviceGroup(),
                data.cameraId(),
                data.description(),
                data.isActive(),
                data.nameByCompany(),
                data.nameByUser()
        );
    }
}
