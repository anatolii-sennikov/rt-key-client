package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.BaseServiceTest;
import io.github.anatoliisennikov.rtkey.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;

public class DeviceServiceImplTest extends BaseServiceTest {
    private static final String DEVICES_PATH = "/api/v4/devices";
    private static final String INTERCOMS_PATH = "/api/v2/app/devices/intercom";
    private static final String RENAME_PATH = "/api/v2/app/devices/intercom/77777";
    private static final String OPEN_PATH = "/api/v2/app/devices/77777/open";

    private DeviceServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new DeviceServiceImpl(transport, tokenSupplier);
    }

    @Test
    public void devicesWithoutIdBindsEmptyQuery() {
        assertReachedTransport(service::devices);

        verifyGetEmptyQuery(Host.HOUSEHOLD, DEVICES_PATH);
    }

    @Test
    public void devicesWithIdBindsIdsFilter() {
        assertReachedTransport(() -> service.devices(77777));

        verifyGet(Host.HOUSEHOLD, DEVICES_PATH, "ids=77777");
    }

    @Test
    public void devicesRejectsNonPositiveId() {
        assertThatThrownBy(() -> service.devices(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id must be positive");
        assertThatThrownBy(() -> service.devices(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id must be positive");

        verifyNoInteractions(transport);
    }

    @Test
    public void intercomsRequestsIntercomListWithoutQuery() {
        assertReachedTransport(service::intercoms);

        verifyGet(Host.HOUSEHOLD, INTERCOMS_PATH);
    }

    @Test
    public void renameIntercomRejectsNonPositiveId() {
        assertThatThrownBy(() -> service.renameIntercom(0, "new name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id must be positive");
        assertThatThrownBy(() -> service.renameIntercom(-1, "new name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id must be positive");

        verifyNoInteractions(transport);
    }

    @Test
    public void renameIntercomBindsIdIntoPathAndNameIntoBody() {
        assertReachedTransport(() -> service.renameIntercom(77777, "new name"));

        verifyPatch(Host.HOUSEHOLD, RENAME_PATH, "{\"name_by_user\":\"new name\"}");
    }

    @Test
    public void openRejectsNonPositiveId() {
        assertThatThrownBy(() -> service.open(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id must be positive");
        assertThatThrownBy(() -> service.open(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id must be positive");

        verifyNoInteractions(transport);
    }

    @Test
    public void openBindsIdIntoPath() {
        assertReachedTransport(() -> service.open(77777));

        verifyPost(Host.HOUSEHOLD, OPEN_PATH);
    }
}
