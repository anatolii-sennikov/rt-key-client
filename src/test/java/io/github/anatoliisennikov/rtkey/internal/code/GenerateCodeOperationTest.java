package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.GeneratedCode;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GenerateCodeOperationTest extends BaseOperationTest {
    private static final String PATH = "/api/v3/app/devices/codes/generate";
    private static final String EXPECTED_BODY = "{\"flat_id\":11223344,\"devices_ids\":[77777]}";

    private final ArgumentCaptor<Body> body = ArgumentCaptor.forClass(Body.class);

    @Test
    public void generateCode() throws IOException {
        final String data = getResourceAsString("/code/code_generate.json");

        when(transport.post(eq(Host.HOUSEHOLD), eq(PATH), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        GeneratedCode generatedCode = GenerateCodeOperation.call(transport, tokenSupplier, 11223344, 77777);

        verify(transport).post(eq(Host.HOUSEHOLD), eq(PATH), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), EXPECTED_BODY);

        GeneratedCode expected = new GeneratedCode(
                48273019,
                "temporary",
                "52748",
                null,
                Instant.parse("2025-08-19T12:37:41Z"),
                63841209,
                List.of(71458),
                847391,
                Instant.parse("2025-08-18T09:21:36Z")
        );

        assertThat(generatedCode).isEqualTo(expected);
    }

    @Test
    public void generateCodeUnknownFlat() throws IOException {
        final String data = getResourceAsString("/code/code_generate_unknown_flat.json");

        when(transport.post(eq(Host.HOUSEHOLD), eq(PATH), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(400, data));

        assertThatThrownBy(() -> GenerateCodeOperation.call(transport, tokenSupplier, 11223344, 77777))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Переданы невалидные данные: unknown flat");

        verify(transport).post(eq(Host.HOUSEHOLD), eq(PATH), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), EXPECTED_BODY);
    }

    @Test
    public void generateCodeFlatWithoutDevices() throws IOException {
        final String data = getResourceAsString("/code/code_generate_flat_without_devices.json");

        when(transport.post(eq(Host.HOUSEHOLD), eq(PATH), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(400, data));

        assertThatThrownBy(() -> GenerateCodeOperation.call(transport, tokenSupplier, 11223344, 77777))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Переданы невалидные данные: flat without devices");

        verify(transport).post(eq(Host.HOUSEHOLD), eq(PATH), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), EXPECTED_BODY);
    }
}
