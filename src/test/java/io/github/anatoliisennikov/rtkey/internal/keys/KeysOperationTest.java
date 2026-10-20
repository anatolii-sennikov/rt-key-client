package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Key;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeysOperationTest extends BaseOperationTest {
    private static final String KEYS_PATH = "/api/v2/app/keys";
    private static final String KEY_PATH = "/api/v2/app/keys/55552501";

    private final ArgumentCaptor<Body> body = ArgumentCaptor.forClass(Body.class);

    @Test
    public void createKey() throws IOException {
        final String data = getResourceAsString("/keys/key.json");

        when(transport.post(eq(Host.HOUSEHOLD), eq(KEYS_PATH), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Key key = KeysOperation.createKey(transport, tokenSupplier, 123, "4457262AEB6D01", "My awesome key");

        verify(transport).post(eq(Host.HOUSEHOLD), eq(KEYS_PATH), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), "{\"flat_id\":123,\"code\":\"4457262AEB6D01\",\"name\":\"My awesome key\"}");

        Key expected = new Key(
                55552501,
                "My awesome key",
                "4457262AEB6D01",
                Instant.parse("2025-09-28T21:38:38.275328213Z"),
                null,
                55554411,
                "tenant",
                "",
                null
        );

        assertThat(key).isEqualTo(expected);
    }

    @Test
    public void createKeyInvalidCodeFormat() {
        final String data = "{\"message\":\"Validation Failure\",\"metas\":[{\"field\":\"code\",\"reason\":\"invalid code format\"}]}";

        when(transport.post(eq(Host.HOUSEHOLD), eq(KEYS_PATH), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(422, data));

        assertThatThrownBy(() -> KeysOperation.createKey(transport, tokenSupplier, 123, "somewrongKey", "My awesome key"))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Validation Failure: code: invalid code format");

        verify(transport).post(eq(Host.HOUSEHOLD), eq(KEYS_PATH), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), "{\"flat_id\":123,\"code\":\"somewrongKey\",\"name\":\"My awesome key\"}");
    }

    @Test
    public void createKeyFlatNotFound() {
        final String data = "{\"error\":{\"code\":\"flat_not_found\",\"description\":\"Квартира не найдена\",\"title\":\"Квартира не найдена\"}}";

        when(transport.post(eq(Host.HOUSEHOLD), eq(KEYS_PATH), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(404, data));

        assertThatThrownBy(() -> KeysOperation.createKey(transport, tokenSupplier, 113, "0117262AEB6D81", "My awesome key"))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Квартира не найдена");

        verify(transport).post(eq(Host.HOUSEHOLD), eq(KEYS_PATH), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), "{\"flat_id\":113,\"code\":\"0117262AEB6D81\",\"name\":\"My awesome key\"}");
    }

    @Test
    public void changeKeyName() throws IOException {
        final String data = getResourceAsString("/keys/key.json");

        when(transport.patch(eq(Host.HOUSEHOLD), eq(KEY_PATH), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Key key = KeysOperation.changeKeyName(transport, tokenSupplier, 55552501, "new name");

        verify(transport).patch(eq(Host.HOUSEHOLD), eq(KEY_PATH), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), "{\"name\":\"new name\"}");

        Key expected = new Key(
                55552501,
                "My awesome key",
                "4457262AEB6D01",
                Instant.parse("2025-09-28T21:38:38.275328213Z"),
                null,
                55554411,
                "tenant",
                "",
                null
        );
        assertThat(key).isEqualTo(expected);
    }

    @Test
    public void changeKeyNameKeyNotFound() {
        final String data = "{\"error\":{\"code\":\"key_not_found\",\"description\":\"Ключ не найден\",\"title\":\"Ключ не найден\"}}";

        when(transport.patch(eq(Host.HOUSEHOLD), eq(KEY_PATH), any(Body.class), same(tokenSupplier)))
                .thenReturn(new Response(404, data));

        assertThatThrownBy(() -> KeysOperation.changeKeyName(transport, tokenSupplier, 55552501, "new name"))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Ключ не найден");

        verify(transport).patch(eq(Host.HOUSEHOLD), eq(KEY_PATH), body.capture(), same(tokenSupplier));
        assertJsonBody(body.getValue(), "{\"name\":\"new name\"}");
    }
}
