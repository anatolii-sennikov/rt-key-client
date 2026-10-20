package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.model.DeviceStatus;
import io.github.anatoliisennikov.rtkey.model.Intercode;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IntercodeDataTest {
    private static final Instant START_DATE = Instant.parse("2025-04-01T06:00:00Z");
    private static final Instant END_DATE = Instant.parse("2025-04-30T22:00:00Z");

    @Test
    public void toModelMapsEveryField() {
        Intercode intercode = IntercodeData.toModel(intercodeData(true, statuses()));

        assertThat(intercode).isEqualTo(new Intercode(
                88004,
                "1357",
                START_DATE,
                END_DATE,
                "permanent",
                true,
                List.of(
                        new DeviceStatus("dev-1", "intercom", "active", "Подъезд 1"),
                        new DeviceStatus("dev-2", "barrier", "inactive", "Шлагбаум 2")
                )
        ));
    }

    @Test
    public void toModelRebuildsEveryStatusAsModelDeviceStatus() {
        Intercode intercode = IntercodeData.toModel(intercodeData(true, statuses()));

        assertThat(intercode.statuses()).containsExactly(
                new DeviceStatus("dev-1", "intercom", "active", "Подъезд 1"),
                new DeviceStatus("dev-2", "barrier", "inactive", "Шлагбаум 2")
        );
    }

    @Test
    public void toModelKeepsTheTwoInstantsApart() {
        Intercode intercode = IntercodeData.toModel(intercodeData(true, statuses()));

        assertThat(intercode.startDate()).isEqualTo(START_DATE);
        assertThat(intercode.endDate()).isEqualTo(END_DATE);
    }

    @Test
    public void toModelKeepsCommonFlag() {
        assertThat(IntercodeData.toModel(intercodeData(true, statuses())).common()).isTrue();
        assertThat(IntercodeData.toModel(intercodeData(false, statuses())).common()).isFalse();
    }

    @Test
    public void toModelKeepsEmptyStatusesEmpty() {
        Intercode intercode = IntercodeData.toModel(intercodeData(true, List.of()));

        assertThat(intercode.statuses()).isEmpty();
    }

    private static List<IntercodeData.Status> statuses() {
        return List.of(
                new IntercodeData.Status("dev-1", "intercom", "active", "Подъезд 1"),
                new IntercodeData.Status("dev-2", "barrier", "inactive", "Шлагбаум 2")
        );
    }

    private static IntercodeData intercodeData(Boolean common, List<IntercodeData.Status> statuses) {
        return new IntercodeData(
                88004,
                "1357",
                START_DATE,
                END_DATE,
                "permanent",
                common,
                statuses
        );
    }
}
