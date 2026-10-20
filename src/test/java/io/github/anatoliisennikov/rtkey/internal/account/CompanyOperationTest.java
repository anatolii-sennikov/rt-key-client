package io.github.anatoliisennikov.rtkey.internal.account;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Company;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class CompanyOperationTest extends BaseOperationTest {

    @Test
    public void flatNotExisted() throws IOException {
        final String data = getResourceAsString("/account/flat_not_existed.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v1/app/company"), eq(Query.of("flat_id", "12345")), same(tokenSupplier)))
                .thenReturn(new Response(400, data));

        assertThatThrownBy(() -> CompanyOperation.call(transport, 12345, tokenSupplier))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("Неверно указана квартира: not exists user flat relation");
    }

    @Test
    public void company() throws IOException {
        final String data = getResourceAsString("/account/company.json");

        when(transport.get(eq(Host.HOUSEHOLD), eq("/api/v1/app/company"), eq(Query.of("flat_id", "90210")), same(tokenSupplier)))
                .thenReturn(new Response(200, data));

        Company company = CompanyOperation.call(transport, 90210, tokenSupplier);
        Company expectedCompany = new Company(1683, "г. ВЕХНИЕ ПИРОЖКИ Победителей Саурона 34 581", "", "ООО ДЕЛАЕМ МИР ЛУЧШЕ");
        assertThat(company).isEqualTo(expectedCompany);
    }

}
