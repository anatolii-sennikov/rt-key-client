package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * An intercom as returned by {@link io.github.anatoliisennikov.rtkey.DeviceService#intercoms()}.
 *
 * @param id            device identifier (a string in this API)
 * @param serialNumber  serial number
 * @param deviceGroup   reported by the server, when set; passed through without interpretation
 * @param cameraId      identifier of the camera associated with the intercom, when set
 * @param description   description, when set
 * @param isActive      whether the intercom is active
 * @param nameByCompany name given by the company
 * @param nameByUser    name given by the user, when set
 */
public record Intercom(
        String id,
        String serialNumber,
        @Nullable
        List<String> deviceGroup,
        @Nullable
        String cameraId,
        @Nullable
        String description,
        Boolean isActive,
        String nameByCompany,
        @Nullable
        String nameByUser
) {
}
