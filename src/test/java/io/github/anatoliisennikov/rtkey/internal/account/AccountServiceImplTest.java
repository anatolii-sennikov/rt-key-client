package io.github.anatoliisennikov.rtkey.internal.account;

import io.github.anatoliisennikov.rtkey.BaseServiceTest;
import io.github.anatoliisennikov.rtkey.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;

public class AccountServiceImplTest extends BaseServiceTest {
    private static final String USER_PATH = "/api/v3/app/users/current";
    private static final String COMPANY_PATH = "/api/v1/app/company";
    private static final String ERROR_MESSAGE = "flatId must be positive";

    private AccountServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new AccountServiceImpl(transport, tokenSupplier);
    }

    @Test
    public void userRequestsCurrentUserWithoutQuery() {
        assertReachedTransport(service::user);

        verifyGet(Host.HOUSEHOLD, USER_PATH);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void companyRejectsNonPositiveFlatId(int flatId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.company(flatId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_MESSAGE);
    }

    @Test
    public void companyBindsFlatIdIntoQuery() {
        assertReachedTransport(() -> service.company(11223344));

        verifyGet(Host.HOUSEHOLD, COMPANY_PATH, "flat_id=11223344");
    }
}
