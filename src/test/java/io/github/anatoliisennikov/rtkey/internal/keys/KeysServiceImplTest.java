package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.BaseServiceTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class KeysServiceImplTest extends BaseServiceTest {
    private static final String RFIDS_PATH = "/api/v5/devices/rfids";
    private static final String RFID_PATH = "/api/v5/devices/rfids/55552501";
    private static final String KEYS_PATH = "/api/v2/app/keys";
    private static final String KEY_PATH = "/api/v2/app/keys/55552501";
    private static final String FLAT_ID_ERROR_MESSAGE = "flatId must be positive";
    private static final String KEY_ID_ERROR_MESSAGE = "keyId must be positive";
    private static final String RFID_ID_ERROR_MESSAGE = "rfidId must be positive";

    private KeysServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new KeysServiceImpl(transport, tokenSupplier);
    }

    @Test
    public void rfidsWithoutQueryBindsEmptyPaging() {
        assertReachedTransport(service::rfids);

        verifyGetEmptyQuery(Host.HOUSEHOLD, RFIDS_PATH);
    }

    @Test
    public void rfidsWithQueryForwardsPaging() {
        LimitOffsetQuery query = LimitOffsetQuery.builder()
                .limit(10)
                .offset(5)
                .build();

        assertReachedTransport(() -> service.rfids(query));

        verifyGet(Host.HOUSEHOLD, RFIDS_PATH, "limit=10&offset=5");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void createKeyRejectsNonPositiveFlatId(int flatId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.createKey(flatId, "0447222AEB6D55", "My awesome key"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FLAT_ID_ERROR_MESSAGE);
    }

    @Test
    public void createKeyBindsFlatIdRfidUidAndName() {
        assertReachedTransport(() -> service.createKey(11223344, "0447222AEB6D55", "My awesome key"));

        verifyPost(Host.HOUSEHOLD, KEYS_PATH,
                "{\"flat_id\":11223344,\"code\":\"0447222AEB6D55\",\"name\":\"My awesome key\"}");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void renameKeyRejectsNonPositiveKeyId(int keyId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.renameKey(keyId, "new name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(KEY_ID_ERROR_MESSAGE);
    }

    @Test
    public void renameKeyBindsKeyIdIntoPathAndNameIntoBody() {
        assertReachedTransport(() -> service.renameKey(55552501, "new name"));

        verifyPatch(Host.HOUSEHOLD, KEY_PATH, "{\"name\":\"new name\"}");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void deleteRfidRejectsNonPositiveKeyId(int keyId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.deleteRfid(keyId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(RFID_ID_ERROR_MESSAGE);
    }

    @Test
    public void deleteRfidBindsKeyIdIntoPath() {
        assertReachedTransport(() -> service.deleteRfid(55552501));

        verifyDelete(Host.HOUSEHOLD, RFID_PATH);
    }
}
