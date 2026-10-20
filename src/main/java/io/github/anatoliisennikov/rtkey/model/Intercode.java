package io.github.anatoliisennikov.rtkey.model;


import java.time.Instant;
import java.util.List;

/**
 * The intercode of a flat, returned by
 * {@link io.github.anatoliisennikov.rtkey.CodeService#intercode(int)}.
 *
 * @param id            identifier
 * @param code          code value
 * @param startDate     start date
 * @param endDate       end date
 * @param interCodeType reported by the server; passed through without interpretation
 * @param common        reported by the server; passed through without interpretation
 * @param statuses      statuses on the devices
 */
public record Intercode(
        Integer id,
        String code,
        Instant startDate,
        Instant endDate,
        String interCodeType,
        Boolean common,
        List<DeviceStatus> statuses

) {
}
