package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.BaseServiceTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.model.CodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;

public class CodeServiceImplTest extends BaseServiceTest {
    private static final String CODES_PATH = "/api/v3/app/devices/codes";
    private static final String GENERATE_PATH = "/api/v3/app/devices/codes/generate";
    private static final String EMERGENCY_PATH = "/api/v3/app/codes/device/77777/type/emergency";
    private static final String INTERCODE_PATH = "/api/v2/app/flats/11223344/intercode";
    private static final String FLAT_ID_ERROR_MESSAGE = "flatId must be positive";
    private static final String DEVICE_ID_ERROR_MESSAGE = "deviceId must be positive";

    private CodeServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new CodeServiceImpl(transport, tokenSupplier);
    }

    @Test
    public void codesWithoutTypeBindsEmptyQuery() {
        assertReachedTransport(service::codes);

        verifyGetEmptyQuery(Host.HOUSEHOLD, CODES_PATH);
    }

    @Test
    public void codesWithTypeBindsTypesFilter() {
        assertReachedTransport(() -> service.codes(CodeType.EMERGENCY));

        verifyGet(Host.HOUSEHOLD, CODES_PATH, "types=emergency");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void generateCodeRejectsNonPositiveFlatId(int flatId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.generateCode(flatId, 77777))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FLAT_ID_ERROR_MESSAGE);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void generateCodeRejectsNonPositiveDeviceId(int deviceId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.generateCode(11223344, deviceId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DEVICE_ID_ERROR_MESSAGE);
    }

    @Test
    public void generateCodeBindsFlatIdAndDeviceId() {
        assertReachedTransport(() -> service.generateCode(11223344, 77777));

        verifyPost(Host.HOUSEHOLD, GENERATE_PATH, "{\"flat_id\":11223344,\"devices_ids\":[77777]}");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void emergencyCodesRejectsNonPositiveDeviceId(int deviceId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.emergencyCodes(deviceId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DEVICE_ID_ERROR_MESSAGE);
    }

    @Test
    public void emergencyCodesBindsDeviceIdIntoPath() {
        assertReachedTransport(() -> service.emergencyCodes(77777));

        verifyGet(Host.HOUSEHOLD, EMERGENCY_PATH);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void intercodeRejectsNonPositiveFlatId(int flatId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.intercode(flatId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FLAT_ID_ERROR_MESSAGE);
    }

    @Test
    public void intercodeBindsFlatIdIntoPath() {
        assertReachedTransport(() -> service.intercode(11223344));

        verifyGet(Host.HOUSEHOLD, INTERCODE_PATH);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void deleteIntercodeRejectsNonPositiveFlatId(int flatId) {
        assertDidNotReachTransport();
        assertThatThrownBy(() -> service.deleteIntercode(flatId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FLAT_ID_ERROR_MESSAGE);
    }

    @Test
    public void deleteIntercodeBindsFlatIdIntoPath() {
        assertReachedTransport(() -> service.deleteIntercode(11223344));

        verifyDelete(Host.HOUSEHOLD, INTERCODE_PATH);
    }
}
