package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;
import io.github.anatoliisennikov.rtkey.model.Page;
import io.github.anatoliisennikov.rtkey.model.Rfid;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RfidsOperationTest extends BaseOperationTest {

    @Test
    public void rfids() throws IOException {
        final String data = getResourceAsString("/keys/rfids.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v5/devices/rfids"), eq(Query.empty()), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Page<Rfid> rfidPage = RfidsOperation.call(transport, tokenSupplier, LimitOffsetQuery.empty());

        Rfid rfid = new Rfid(
                55577780,
                "Ключ мой ключ ",
                "0447222AEB6D55",
                List.of(77705511),
                List.of(20001),
                "master",
                "free",
                List.of(new Rfid.DeviceStatus(11558, 22233344, "loaded", Instant.parse("2025-11-10T16:20:09Z"))),
                List.of(9911),
                Instant.parse("2025-11-10T16:18:54Z"),
                Instant.parse("2025-11-10T19:20:08Z")
        );
        Page<Rfid> expected = new Page<>(List.of(rfid), 1);
        assertThat(rfidPage).isEqualTo(expected);
    }

    @Test
    public void deleteRfid() {
        when(transport.delete(eq(Host.HOUSEHOLD), eq("/api/v5/devices/rfids/98712"), same(tokenSupplier)))
                .thenReturn(new Response(200, ""));

        assertThatCode(() -> RfidsOperation.deleteRfid(transport, tokenSupplier, 98712))
                .doesNotThrowAnyException();

        verify(transport).delete(eq(Host.HOUSEHOLD), eq("/api/v5/devices/rfids/98712"), same(tokenSupplier));
    }
}
