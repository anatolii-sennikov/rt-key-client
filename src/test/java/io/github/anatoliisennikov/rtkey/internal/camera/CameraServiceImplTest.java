package io.github.anatoliisennikov.rtkey.internal.camera;

import io.github.anatoliisennikov.rtkey.BaseServiceTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.model.CameraData;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CameraServiceImplTest extends BaseServiceTest {
    private static final String CAMERAS_PATH = "/vc/api/v1/camera_video_data/list";
    private static final String ARCHIVE_CALENDAR_PATH =
            "/api/v1/cameras/" + CameraDataFixtures.CAMERA_UID + "/archive_calendar";
    private static final String ARCHIVE_INTERVALS_PATH =
            "/api/v1/cameras/" + CameraDataFixtures.CAMERA_UID + "/archive_intervals";

    private CameraServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new CameraServiceImpl(transport, tokenSupplier);
    }

    @Test
    public void archiveCalendarBindsCameraUidIntoPath() {
        assertReachedTransport(() -> service.archiveCalendar(CameraDataFixtures.CAMERA_UID));

        verifyGet(Host.VC, ARCHIVE_CALENDAR_PATH);
    }

    @Test
    public void archiveIntervalBindsCameraUidIntoPath() {
        assertReachedTransport(() -> service.archiveIntervals(CameraDataFixtures.CAMERA_UID));

        verifyGet(Host.VC, ARCHIVE_INTERVALS_PATH);
    }

    @Test
    public void camerasWithoutQueryBindsEmptyPaging() {
        assertReachedTransport(service::cameras);

        verifyGetEmptyQuery(Host.KEYAPIS, CAMERAS_PATH);
    }

    @Test
    public void camerasWithQueryForwardsPaging() {
        LimitOffsetQuery query = LimitOffsetQuery.builder()
                .limit(10)
                .offset(5)
                .build();

        assertReachedTransport(() -> service.cameras(query));

        verifyGet(Host.KEYAPIS, CAMERAS_PATH, "paging.limit=10&paging.offset=5");
    }
}
