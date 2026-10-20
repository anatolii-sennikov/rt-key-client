package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.model.Intercom;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IntercomDataTest {
    private static final Instant START_DATE = Instant.parse("2025-04-01T06:00:00Z");
    private static final Instant END_DATE = Instant.parse("2025-04-30T22:00:00Z");

    @Test
    public void toModelMapsEveryPublicField() {
        Intercom intercom = IntercomData.toModel(intercomData(
                List.of("group-1", "group-2"),
                "camera-1",
                "Подъезд 1",
                "Мой домофон",
                capabilities(),
                interCodes()
        ));

        assertThat(intercom).isEqualTo(new Intercom(
                "intercom-1",
                "SN-0001",
                List.of("group-1", "group-2"),
                "camera-1",
                "Подъезд 1",
                true,
                "Домофон у подъезда",
                "Мой домофон"
        ));
    }

    @Test
    public void toModelMapsActiveFlagAndNotTheFavouriteOne() {
        Intercom intercom = IntercomData.toModel(intercomData(
                List.of("group-1"),
                "camera-1",
                "Подъезд 1",
                "Мой домофон",
                capabilities(),
                interCodes()
        ));

        assertThat(intercom.isActive()).isTrue();
    }

    @Test
    public void toModelKeepsNullableFieldsAbsent() {
        Intercom intercom = IntercomData.toModel(intercomData(
                null, null, null, null, capabilities(), interCodes()
        ));

        assertThat(intercom.deviceGroup()).isNull();
        assertThat(intercom.cameraId()).isNull();
        assertThat(intercom.description()).isNull();
        assertThat(intercom.nameByUser()).isNull();
        assertThat(intercom.id()).isEqualTo("intercom-1");
        assertThat(intercom.serialNumber()).isEqualTo("SN-0001");
        assertThat(intercom.nameByCompany()).isEqualTo("Домофон у подъезда");
    }

    @Test
    public void toModelKeepsEmptyDeviceGroupEmpty() {
        Intercom intercom = IntercomData.toModel(intercomData(
                List.of(), "camera-1", "Подъезд 1", "Мой домофон", capabilities(), interCodes()
        ));

        assertThat(intercom.deviceGroup()).isEmpty();
    }

    @Test
    public void toModelIgnoresNestedCapabilitiesAndInterCodes() {
        Intercom withNestedStructures = IntercomData.toModel(intercomData(
                List.of("group-1", "group-2"),
                "camera-1",
                "Подъезд 1",
                "Мой домофон",
                capabilities(),
                interCodes()
        ));
        Intercom withoutNestedStructures = IntercomData.toModel(intercomData(
                List.of("group-1", "group-2"),
                "camera-1",
                "Подъезд 1",
                "Мой домофон",
                List.of(),
                List.of()
        ));

        assertThat(withNestedStructures).isEqualTo(withoutNestedStructures);
    }

    private static List<IntercomData.Capability> capabilities() {
        return List.of(
                new IntercomData.Capability("open_door", true),
                new IntercomData.Capability("video", false)
        );
    }

    private static List<InterCode> interCodes() {
        return List.of(
                new InterCode(1, "1234", START_DATE, END_DATE, "permanent", "Постоянный код", "Код соседа"),
                new InterCode(2, "5678", null, null, "temporary", "Временный код", null)
        );
    }

    private static IntercomData intercomData(@Nullable List<String> deviceGroup,
                                             @Nullable String cameraId,
                                             @Nullable String description,
                                             @Nullable String nameByUser,
                                             @Nullable List<IntercomData.Capability> capabilities,
                                             List<InterCode> interCodes) {
        return new IntercomData(
                "intercom-1",
                "intercom",
                "SN-0001",
                deviceGroup,
                180,
                cameraId,
                description,
                false,
                true,
                "Домофон у подъезда",
                nameByUser,
                true,
                capabilities,
                interCodes
        );
    }
}
