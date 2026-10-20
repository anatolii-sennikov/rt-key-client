package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.model.GeneratedCode;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GenerateDataTest {
    private static final Instant CREATED_AT = Instant.parse("2025-03-01T08:00:00Z");
    private static final Instant EXPIRES_AT = Instant.parse("2025-03-08T20:30:00Z");

    @Test
    public void toModelMapsEveryField() {
        GenerateData data = new GenerateData(
                88003,
                "temporary",
                "2468",
                "Код для гостя",
                EXPIRES_AT,
                303,
                List.of(9001, 9002),
                7001,
                CREATED_AT
        );

        GeneratedCode code = GenerateData.toModel(data);

        assertThat(code).isEqualTo(new GeneratedCode(
                88003,
                "temporary",
                "2468",
                "Код для гостя",
                EXPIRES_AT,
                303,
                List.of(9001, 9002),
                7001,
                CREATED_AT
        ));
    }

    @Test
    public void toModelKeepsTheTwoInstantsApart() {
        GenerateData data = new GenerateData(
                88003,
                "temporary",
                "2468",
                "Код для гостя",
                EXPIRES_AT,
                303,
                List.of(9001),
                7001,
                CREATED_AT
        );

        GeneratedCode code = GenerateData.toModel(data);

        assertThat(code.createdAt()).isEqualTo(CREATED_AT);
        assertThat(code.expiresAt()).isEqualTo(EXPIRES_AT);
    }

    @Test
    public void toModelKeepsAbsentTitleNull() {
        GenerateData data = new GenerateData(
                88003,
                "temporary",
                "2468",
                null,
                EXPIRES_AT,
                303,
                List.of(9001),
                7001,
                CREATED_AT
        );

        assertThat(GenerateData.toModel(data).title()).isNull();
    }

    @Test
    public void toModelKeepsEmptyDeviceIdsEmpty() {
        GenerateData data = new GenerateData(
                88003,
                "temporary",
                "2468",
                "Код для гостя",
                EXPIRES_AT,
                303,
                List.of(),
                7001,
                CREATED_AT
        );

        assertThat(GenerateData.toModel(data).deviceIds()).isEmpty();
    }
}
