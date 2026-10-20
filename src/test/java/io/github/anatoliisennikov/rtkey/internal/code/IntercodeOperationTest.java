package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.DeviceStatus;
import io.github.anatoliisennikov.rtkey.model.Intercode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IntercodeOperationTest extends BaseOperationTest {

    @Test
    public void intercode() throws IOException {
        final String data = getResourceAsString("/code/intercode.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v2/app/flats/444444/intercode"), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Intercode intercode = IntercodeOperation.getIntercode(transport, tokenSupplier, 444444);

        Intercode expected = new Intercode(
                24223091,
                "84072",
                Instant.parse("2026-10-10T04:04:59.106587Z"),
                Instant.parse("2026-10-11T04:04:59.106587Z"),
                "temporary",
                true,
                List.of(new DeviceStatus("55119", "intercom", "loaded", "piupiu"))
        );

        assertThat(intercode).isEqualTo(expected);
    }

    @Test
    public void deleteIntercode() {
        when(transport.delete(eq(Host.HOUSEHOLD), eq("/api/v2/app/flats/444444/intercode"), same(tokenSupplier)))
                .thenReturn(new Response(200, ""));

        assertThatCode(() -> IntercodeOperation.deleteIntercode(transport, tokenSupplier, 444444))
                .doesNotThrowAnyException();

        verify(transport).delete(eq(Host.HOUSEHOLD), eq("/api/v2/app/flats/444444/intercode"), same(tokenSupplier));
    }

    @Test
    public void intercodeFlatNotFound() throws IOException {
        final String data = getResourceAsString("/code/intercode_flat_not_found.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v2/app/flats/444444/intercode"), same(tokenSupplier)))
                .thenReturn(new Response(404, data));

        assertThatThrownBy(() -> IntercodeOperation.getIntercode(transport, tokenSupplier, 444444))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Квартира не найдена");
    }
}
