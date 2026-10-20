package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Privilege;
import io.github.anatoliisennikov.rtkey.model.PrivilegeQuery;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class PrivilegeListOperationTest extends BaseOperationTest {

    @Test
    public void privileges() throws IOException {
        final String data = getResourceAsString("/subscription/privileges.json");

        when(transport.get(eq(Host.KEYAPIS), eq("/subscription/api/v1/privilege/list"), eq(Query.empty()), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<Privilege> privileges = PrivilegeListOperation.call(transport, tokenSupplier, PrivilegeQuery.empty());

        assertThat(privileges).isEqualTo(expectedPrivileges());
    }

    @Test
    public void privilegesWithQuery() throws IOException {
        final String data = getResourceAsString("/subscription/privileges.json");

        PrivilegeQuery query = PrivilegeQuery.builder()
                .limit(10)
                .flatNumbers("136")
                .orpons("113156294")
                .build();

        Query expectedQuery = Query.empty()
                .add("paging.limit", "10")
                .add("filter.flatNumbers", "136")
                .add("filter.orpons", "113156294");

        when(transport.get(eq(Host.KEYAPIS), eq("/subscription/api/v1/privilege/list"), eq(expectedQuery), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        List<Privilege> privileges = PrivilegeListOperation.call(transport, tokenSupplier, query);

        assertThat(privileges).isEqualTo(expectedPrivileges());
    }

    private static List<Privilege> expectedPrivileges() {
        return List.of(
                new Privilege("777555111222", "9", "INTERCOM_CALLREDIRECT_PSTN", "PAID"),
                new Privilege("777555111222", "9", "INTERCOM_RFID_USE", "CAN_BE_PAID"),
                new Privilege("777555111222", "9", "INTERCOM_USE", "CAN_BE_PAID"),
                new Privilege("777555111222", "8", "INTERCOM_CALLREDIRECT_PSTN", "PAID"),
                new Privilege("777555111222", "8", "INTERCOM_RFID_USE", "CAN_BE_PAID"),
                new Privilege("777555111222", "8", "INTERCOM_USE", "CAN_BE_PAID"),
                new Privilege("777555111222", "69", "INTERCOM_CALLREDIRECT_PSTN", "CAN_BE_PAID"),
                new Privilege("777555111222", "69", "INTERCOM_RFID_USE", "PAID"),
                new Privilege("777555111222", "69", "INTERCOM_OPEN", "PAID"),
                new Privilege("777555111222", "69", "INTERCOM_USE", "PAID"),
                new Privilege("777555111222", "6668", "INTERCOM_CALLREDIRECT_PSTN", "CAN_BE_PAID"),
                new Privilege("777555111222", "6", "INTERCOM_CALLREDIRECT_PSTN", "CAN_BE_PAID"),
                new Privilege("777555111222", "6", "INTERCOM_RFID_USE", "CAN_BE_PAID"),
                new Privilege("777555111222", "6", "INTERCOM_OPEN", "CAN_BE_PAID"),
                new Privilege("777555111222", "6", "INTERCOM_USE", "CAN_BE_PAID"),
                new Privilege("777555111222", "5", "INTERCOM_CALLREDIRECT_PSTN", "PAID"),
                new Privilege("777555111222", "5", "INTERCOM_RFID_USE", "CAN_BE_PAID"),
                new Privilege("777555111222", "5", "INTERCOM_USE", "CAN_BE_PAID"),
                new Privilege("777555111222", "4", "INTERCOM_CALLREDIRECT_PSTN", "PAID"),
                new Privilege("777555111222", "4", "INTERCOM_RFID_USE", "CAN_BE_PAID")
        );
    }
}
