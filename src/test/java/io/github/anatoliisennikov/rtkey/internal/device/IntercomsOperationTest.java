package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Intercom;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class IntercomsOperationTest extends BaseOperationTest {

    @Test
    public void intercomsList() throws IOException {
        final String data = getResourceAsString("/device/intercom.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v2/app/devices/intercom"), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<Intercom> intercoms = IntercomsOperation.call(transport, tokenSupplier);

        Intercom expectedIntercom = new Intercom(
                "77799",
                "9d227654efe4",
                List.of("Калитка"),
                "174f7cff-6771-4abb-993d-0c25fa08c66b",
                "подъезд №1",
                true,
                "подъезд №1",
                "myCustomName"
        );

        List<Intercom> expected = List.of(expectedIntercom);
        assertThat(intercoms).isEqualTo(expected);
    }
}
