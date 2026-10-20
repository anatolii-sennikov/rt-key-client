package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Code;
import io.github.anatoliisennikov.rtkey.model.CodeType;
import io.github.anatoliisennikov.rtkey.model.Page;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class CodeOperationTest extends BaseOperationTest {

    @Test
    public void allCodes() throws IOException {
        final String data = getResourceAsString("/code/codes.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v3/app/devices/codes"), eq(Query.empty()), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Page<Code> codePage = CodeOperation.call(transport, tokenSupplier, null);

        Code emergencyCode = new Code(
                58391724,
                "38291",
                "emergency",
                null,
                null,
                null,
                "system",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(new Code.Status(47283, 2951, "корпус №3", "loaded")),
                Instant.parse("2025-09-12T06:48:23Z"),
                Instant.parse("2025-09-19T06:48:23Z")
        );

        Code temporaryCode = new Code(
                69104837,
                "16503",
                "temporary",
                null,
                8472910,
                2951,
                "master",
                4718263,
                "rtkid_4827193650482",
                "+79999999999",
                "active",
                4718263,
                "rtkid_4827193650482",
                "+79999999999",
                "active",
                List.of(new Code.Status(47283, 2951, "zoomtest", "loaded")),
                Instant.parse("2025-09-15T18:32:07Z"),
                Instant.parse("2025-09-16T18:32:07Z")
        );

        Page<Code> expected = new Page<>(List.of(emergencyCode, temporaryCode), 2);
        assertThat(codePage).isEqualTo(expected);
    }

    @Test
    public void codesFilteredByEmergencyType() throws IOException {
        final String data = getResourceAsString("/code/codes_types_emergency.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v3/app/devices/codes"),
                eq(Query.of("types", "emergency")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Page<Code> codePage = CodeOperation.call(transport, tokenSupplier, CodeType.EMERGENCY);

        Code expectedCode = new Code(
                58391724,
                "38291",
                "emergency",
                null,
                null,
                null,
                "system",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                List.of(new Code.Status(47283, 2951, "корпус №3", "loaded")),
                Instant.parse("2025-09-12T06:48:23Z"),
                Instant.parse("2025-09-19T06:48:23Z")
        );

        Page<Code> expected = new Page<>(List.of(expectedCode), 1);
        assertThat(codePage).isEqualTo(expected);
    }


    @Test
    public void temporaryCodes() throws IOException {
        final String data = getResourceAsString("/code/codes_types_temporary.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v3/app/devices/codes"),
                eq(Query.of("types", "temporary")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Page<Code> codePage = CodeOperation.call(transport, tokenSupplier, CodeType.TEMPORARY);

        Code expectedCode = new Code(
                69104837,
                "16503",
                "temporary",
                null,
                8472910,
                2951,
                "master",
                4718263,
                "rtkid_4827193650482",
                "+79999999999",
                "active",
                4718263,
                "rtkid_4827193650482",
                "+79999999999",
                "active",
                List.of(new Code.Status(47283, 2951, "zoomtest", "loaded")),
                Instant.parse("2025-09-15T18:32:07Z"),
                Instant.parse("2025-09-16T18:32:07Z")
        );

        Page<Code> expected = new Page<>(List.of(expectedCode), 1);
        assertThat(codePage).isEqualTo(expected);
    }
}
