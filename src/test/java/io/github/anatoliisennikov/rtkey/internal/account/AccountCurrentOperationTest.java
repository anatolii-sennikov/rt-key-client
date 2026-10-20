package io.github.anatoliisennikov.rtkey.internal.account;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Flat;
import io.github.anatoliisennikov.rtkey.model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class AccountCurrentOperationTest extends BaseOperationTest {

    @Test
    public void currentUser() throws IOException {
        final String data = getResourceAsString("/account/user_response.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v3/app/users/current"), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        User user = AccountCurrentOperation.call(transport, tokenSupplier);
        User expectedUser = new User(
                1491111,
                "rtkid_9999888877661",
                "+79999999999",
                "active",
                true,
                false,
                new User.Company(1683, "ООО ДЕЛАЕМ МИР ЛУЧШЕ"),
                List.of(new Flat(
                        77355775, "750",
                        9999, 223555244, "г. ВЕХНИЕ ПИРОЖКИ", "ул. ВЛАСТЕЛИНА КОЛЕЦ, д. 156/24",
                        "1", "1",
                        "1", "1"))
        );

        assertThat(user).isEqualTo(expectedUser);
    }
}