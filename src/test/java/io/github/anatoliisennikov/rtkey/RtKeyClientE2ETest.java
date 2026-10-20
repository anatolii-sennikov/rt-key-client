package io.github.anatoliisennikov.rtkey;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import io.github.anatoliisennikov.rtkey.internal.camera.CameraDataFixtures;
import io.github.anatoliisennikov.rtkey.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;


public class RtKeyClientE2ETest {

    private static final String TOKEN = "bearer.test.token";

    private static WireMockServer keyapis;
    private static WireMockServer household;
    private static WireMockServer vc;
    private static WireMockServer events;

    private static RtKeyClient client;

    @BeforeAll
    static void startServers() {
        keyapis = server();
        household = server();
        vc = server();
        events = server();

        client = RtKeyClient.builder(() -> TOKEN)
                .baseUri(Host.KEYAPIS, URI.create("http://127.0.0.1:" + keyapis.port() + "/"))
                .baseUri(Host.HOUSEHOLD, URI.create("http://127.0.0.1:" + household.port() + "/"))
                .baseUri(Host.VC, URI.create("http://127.0.0.1:" + vc.port() + "/"))
                .baseUri(Host.EVENTS, URI.create("http://127.0.0.1:" + events.port() + "/"))
                .build();
    }

    @AfterAll
    static void stopServers() {
        client.close();
        keyapis.stop();
        household.stop();
        vc.stop();
        events.stop();
    }

    @BeforeEach
    void reset() {
        keyapis.resetAll();
        household.resetAll();
        vc.resetAll();
        events.resetAll();
    }

    private static WireMockServer server() {
        WireMockServer s = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        s.start();
        return s;
    }

    private static void stubGet(WireMockServer wm, String path, String body) {
        wm.stubFor(get(urlPathEqualTo(path))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(body)));
    }

    private static void stubPost(WireMockServer wm, String path, String body) {
        wm.stubFor(post(urlPathEqualTo(path))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(body)));
    }

    private static void stubPatch(WireMockServer wm, String path, String body) {
        wm.stubFor(patch(urlPathEqualTo(path))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(body)));
    }

    private static void stubDelete(WireMockServer wm, String path) {
        wm.stubFor(delete(urlPathEqualTo(path))
                .willReturn(aResponse().withStatus(200)));
    }

    private static RequestPatternBuilder getVerify(String path) {
        return getRequestedFor(urlPathEqualTo(path)).withHeader("Authorization", equalTo("Bearer " + TOKEN));
    }

    private static RequestPatternBuilder postVerify(String path) {
        return postRequestedFor(urlPathEqualTo(path)).withHeader("Authorization", equalTo("Bearer " + TOKEN));
    }

    private static RequestPatternBuilder patchVerify(String path) {
        return patchRequestedFor(urlPathEqualTo(path)).withHeader("Authorization", equalTo("Bearer " + TOKEN));
    }

    private static RequestPatternBuilder deleteVerify(String path) {
        return deleteRequestedFor(urlPathEqualTo(path)).withHeader("Authorization", equalTo("Bearer " + TOKEN));
    }

    private static String fixture(String path) {
        try (InputStream in = Objects.requireNonNull(
                RtKeyClientE2ETest.class.getResourceAsStream(path), "fixture not found: " + path)) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read fixture: " + path, e);
        }
    }

    private static byte[] readAll(CameraStream stream) {
        try (stream) {
            return stream.body().readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read camera stream", e);
        }
    }

    @Test
    public void userReachesHousehold() {
        stubGet(household, "/api/v3/app/users/current", fixture("/account/user_response.json"));

        User user = client.account().user();

        User expected = new User(
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
        assertThat(user).isEqualTo(expected);
        household.verify(getVerify("/api/v3/app/users/current"));
    }

    @Test
    public void companyReachesHousehold() {
        stubGet(household, "/api/v1/app/company", fixture("/account/company.json"));

        Company company = client.account().company(11303565);

        assertThat(company).isEqualTo(new Company(
                1683, "г. ВЕХНИЕ ПИРОЖКИ Победителей Саурона 34 581", "", "ООО ДЕЛАЕМ МИР ЛУЧШЕ"));
        household.verify(getVerify("/api/v1/app/company"));
    }

    @Test
    public void devicesReachesHousehold() {
        stubGet(household, "/api/v4/devices", fixture("/device/device.json"));

        Page<Device> devices = client.devices().devices();

        assertThat(devices).isEqualTo(new Page<>(List.of(expectedDevice()), 1));
        household.verify(getVerify("/api/v4/devices"));
    }

    @Test
    public void devicesByIdsReachesHousehold() {
        stubGet(household, "/api/v4/devices", fixture("/device/device.json"));

        Page<Device> devices = client.devices().devices(42);

        assertThat(devices).isEqualTo(new Page<>(List.of(expectedDevice()), 1));
        household.verify(getVerify("/api/v4/devices"));
    }

    @Test
    public void intercomsReachesHousehold() {
        stubGet(household, "/api/v2/app/devices/intercom", fixture("/device/intercom.json"));

        List<Intercom> intercoms = client.devices().intercoms();

        assertThat(intercoms).isEqualTo(List.of(new Intercom(
                "77799",
                "9d227654efe4",
                List.of("Калитка"),
                "174f7cff-6771-4abb-993d-0c25fa08c66b",
                "подъезд №1",
                true,
                "подъезд №1",
                "myCustomName")));
        household.verify(getVerify("/api/v2/app/devices/intercom"));
    }

    @Test
    public void renameIntercomReachesHousehold() {
        stubPatch(household, "/api/v2/app/devices/intercom/26488", fixture("/device/rename_device.json"));

        Intercom intercom = client.devices().renameIntercom(26488, "new name");

        assertThat(intercom).isEqualTo(new Intercom(
                "99911",
                "1c555550qwe2",
                List.of("Калитка"),
                "27d056d3-c00f-42c6-8a42-ad0d3905117a",
                "подъезд №1",
                true,
                "подъезд №1",
                "new name"));
        household.verify(patchVerify("/api/v2/app/devices/intercom/26488"));
    }

    @Test
    public void openReachesHousehold() {
        stubPost(household, "/api/v2/app/devices/123456/open", "");

        client.devices().open(123456);

        household.verify(postVerify("/api/v2/app/devices/123456/open"));
    }

    @Test
    public void camerasReachesKeyapis() {
        stubGet(keyapis, "/vc/api/v1/camera_video_data/list", fixture("/camera/camera_video_data_list.json"));

        List<CameraData> cameras = client.cameras().cameras();

        assertThat(cameras).isEqualTo(List.of(CameraDataFixtures.camera()));
        keyapis.verify(getVerify("/vc/api/v1/camera_video_data/list"));
    }

    @Test
    public void archiveCalendarReachesVc() {
        UUID uid = CameraDataFixtures.CAMERA_UID;
        stubGet(vc, "/api/v1/cameras/" + uid + "/archive_calendar", fixture("/camera/archive_calendar.json"));

        List<LocalDate> calendar = client.cameras().archiveCalendar(uid);

        assertThat(calendar).isEqualTo(List.of(
                LocalDate.of(2026, 10, 10),
                LocalDate.of(2026, 10, 11),
                LocalDate.of(2026, 10, 12),
                LocalDate.of(2026, 10, 13),
                LocalDate.of(2026, 10, 14),
                LocalDate.of(2026, 10, 15),
                LocalDate.of(2026, 10, 16),
                LocalDate.of(2026, 10, 17)));
        vc.verify(getVerify("/api/v1/cameras/" + uid + "/archive_calendar"));
    }

    @Test
    public void archiveIntervalsReachesVc() {
        UUID uid = CameraDataFixtures.CAMERA_UID;
        stubGet(vc, "/api/v1/cameras/" + uid + "/archive_intervals", fixture("/camera/archive_intervals.json"));

        List<ArchiveInterval> intervals = client.cameras().archiveIntervals(uid);

        assertThat(intervals).hasSize(58);
        assertThat(intervals.getFirst()).isEqualTo(new ArchiveInterval(
                Instant.ofEpochSecond(1791604022L, 0L),
                Instant.ofEpochSecond(1791605940L, 267922200L)));
        assertThat(intervals.get(57)).isEqualTo(new ArchiveInterval(
                Instant.ofEpochSecond(1792179397L, 713044400L),
                Instant.ofEpochSecond(1792208820L, 25044400L)));
        vc.verify(getVerify("/api/v1/cameras/" + uid + "/archive_intervals"));
    }

    @Test
    public void screenshotReachesMediaUrlWithCookie() {
        String tpl = "http://127.0.0.1:" + vc.port() + "/image/{size}/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/{timestamp}.jpg?token={cdn_token}";
        CameraData cam = CameraDataFixtures.camera(tpl, tpl, CameraDataFixtures.STREAMER_URL);
        vc.stubFor(get(urlPathEqualTo("/image/small/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/last.jpg"))
                .withQueryParam("token", equalTo(CameraDataFixtures.SCREENSHOT_TOKEN))
                .willReturn(aResponse().withStatus(200).withBody("jpg-bytes")));

        byte[] screenshot = client.cameraMedia().screenshot(cam, ScreenshotSize.SMALL, null);

        assertThat(screenshot).isEqualTo("jpg-bytes".getBytes(StandardCharsets.UTF_8));
        vc.verify(getRequestedFor(urlPathEqualTo("/image/small/7a327c4a-3b40-4ccb-a9fe-5efbeaa8e1a4/last.jpg"))
                .withHeader("Cookie", equalTo("utoken=" + CameraDataFixtures.USER_TOKEN)));
    }

    @Test
    public void liveReachesStreamerUrl() {
        String streamerUrl = "http://127.0.0.1:" + vc.port() + "/";
        CameraData cam = CameraDataFixtures.camera(
                "http://127.0.0.1:" + vc.port() + "/x",
                "http://127.0.0.1:" + vc.port() + "/y",
                streamerUrl);
        String path = "/stream/" + CameraDataFixtures.CAMERA_UID + "/live.mp4";
        vc.stubFor(get(urlPathEqualTo(path)).willReturn(aResponse().withStatus(200).withBody("stream-bytes")));

        byte[] body = readAll(client.cameraMedia().live(cam));

        assertThat(body).isEqualTo("stream-bytes".getBytes(StandardCharsets.UTF_8));
        vc.verify(getRequestedFor(urlPathEqualTo(path)));
    }

    @Test
    public void archiveReachesStreamerUrl() {
        CameraData cam = CameraDataFixtures.camera(
                "http://127.0.0.1:" + vc.port() + "/x",
                "http://127.0.0.1:" + vc.port() + "/y",
                "http://127.0.0.1:" + vc.port() + "/");
        Instant from = Instant.ofEpochSecond(1700000000L);
        String path = "/stream/" + CameraDataFixtures.CAMERA_UID + "/" + from.getEpochSecond() + ".mp4";
        vc.stubFor(get(urlPathEqualTo(path)).willReturn(aResponse().withStatus(200).withBody("archive-bytes")));

        byte[] body = readAll(client.cameraMedia().archive(cam, from));

        assertThat(body).isEqualTo("archive-bytes".getBytes(StandardCharsets.UTF_8));
        vc.verify(getRequestedFor(urlPathEqualTo(path)));
    }

    @Test
    public void codesReachesHousehold() {
        stubGet(household, "/api/v3/app/devices/codes", fixture("/code/codes.json"));

        Page<Code> codes = client.codes().codes();

        Code emergencyCode = new Code(
                58391724, "38291", "emergency", null, null, null, "system", null,
                null, null, null, null, null, null, null,
                List.of(new Code.Status(47283, 2951, "корпус №3", "loaded")),
                Instant.parse("2025-09-12T06:48:23Z"),
                Instant.parse("2025-09-19T06:48:23Z"));
        Code temporaryCode = new Code(
                69104837, "16503", "temporary", null, 8472910, 2951, "master", 4718263,
                "rtkid_4827193650482", "+79999999999", "active", 4718263,
                "rtkid_4827193650482", "+79999999999", "active",
                List.of(new Code.Status(47283, 2951, "zoomtest", "loaded")),
                Instant.parse("2025-09-15T18:32:07Z"),
                Instant.parse("2025-09-16T18:32:07Z"));
        assertThat(codes).isEqualTo(new Page<>(List.of(emergencyCode, temporaryCode), 2));
        household.verify(getVerify("/api/v3/app/devices/codes"));
    }

    @Test
    public void generateCodeReachesHousehold() {
        stubPost(household, "/api/v3/app/devices/codes/generate", fixture("/code/code_generate.json"));

        GeneratedCode generated = client.codes().generateCode(11303565, 26488);

        assertThat(generated).isEqualTo(new GeneratedCode(
                48273019, "temporary", "52748", null,
                Instant.parse("2025-08-19T12:37:41Z"), 63841209, List.of(71458), 847391,
                Instant.parse("2025-08-18T09:21:36Z")));
        household.verify(postVerify("/api/v3/app/devices/codes/generate"));
    }

    @Test
    public void emergencyCodesReachesHousehold() {
        stubGet(household, "/api/v3/app/codes/device/26488/type/emergency",
                fixture("/code/emergency_code_by_deviceId.json"));

        List<EmergencyCode> codes = client.codes().emergencyCodes(26488);

        assertThat(codes).isEqualTo(List.of(new EmergencyCode(
                22349991, "99014", "emergency", null, null, null, "system", null, "system", null,
                Instant.parse("2025-11-07T18:36:06Z"),
                Instant.parse("2025-11-07T18:36:06Z"),
                Instant.parse("2025-11-14T18:36:06Z"),
                List.of(new EmergencyCode.Status(
                        new EmergencyCode.Device(65758, new EmergencyCode.Company(3399), "intercom",
                                "подъезд №4", null, null),
                        Instant.parse("2025-11-07T18:36:06Z"),
                        "loaded")))));
        household.verify(getVerify("/api/v3/app/codes/device/26488/type/emergency"));
    }

    @Test
    public void intercodeReachesHousehold() {
        stubGet(household, "/api/v2/app/flats/444444/intercode", fixture("/code/intercode.json"));

        Intercode intercode = client.codes().intercode(444444);

        assertThat(intercode).isEqualTo(new Intercode(
                24223091, "84072",
                Instant.parse("2026-10-10T04:04:59.106587Z"),
                Instant.parse("2026-10-11T04:04:59.106587Z"),
                "temporary", true,
                List.of(new DeviceStatus("55119", "intercom", "loaded", "piupiu"))));
        household.verify(getVerify("/api/v2/app/flats/444444/intercode"));
    }

    @Test
    public void deleteIntercodeReachesHousehold() {
        stubDelete(household, "/api/v2/app/flats/444444/intercode");

        client.codes().deleteIntercode(444444);

        household.verify(deleteVerify("/api/v2/app/flats/444444/intercode"));
    }

    @Test
    public void bellCountReachesKeyapis() {
        stubGet(keyapis, "/bell/api/v1/notification/count", fixture("/events/bell_count.json"));

        assertThat(client.events().bellCount()).isEqualTo(280);

        keyapis.verify(getVerify("/bell/api/v1/notification/count"));
    }

    @Test
    public void deviceEventReachesHousehold() {
        stubGet(household, "/api/v1/app/devices/26488/events", fixture("/events/devices_events.json"));

        List<DeviceEvent> deviceEvents = client.events().deviceEvent(26488);

        assertThat(deviceEvents).isEqualTo(List.of(
                new DeviceEvent(11133, "intercom", 1771999, Instant.parse("2025-11-10T12:05:27Z"), 11,
                        "Открыта основная дверь с помощью удаленного доступа", "", "11"),
                new DeviceEvent(11133, "intercom", 1771999, Instant.parse("2025-11-10T11:29:13Z"), 11,
                        "Открыта основная дверь с помощью удаленного доступа", "", "11"),
                new DeviceEvent(11133, "intercom", 1771999, Instant.parse("2025-11-10T09:44:16Z"), 11,
                        "Открыта основная дверь с помощью удаленного доступа", "", "11")));
        household.verify(getVerify("/api/v1/app/devices/26488/events"));
    }

    @Test
    public void eventsAmountReachesEvents() {
        stubGet(events, "/api/v2/events/amount", fixture("/events/events_amount.json"));

        int amount = client.events().eventsAmount(Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-02T00:00:00Z"));

        assertThat(amount).isEqualTo(43);
        events.verify(getVerify("/api/v2/events/amount"));
    }

    @Test
    public void eventsListReachesEvents() {
        stubGet(events, "/api/v2/events/list", fixture("/events/events_list.json"));

        List<Event> list = client.events().events(Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-02T00:00:00Z"));

        assertThat(list).hasSize(20);
        events.verify(getVerify("/api/v2/events/list"));
    }

    @Test
    public void offersReachesKeyapis() {
        stubGet(keyapis, "/subscription/api/v1/offer/list", fixture("/subscription/offers.json"));

        assertThat(client.subscriptions().offers()).hasSize(20);

        keyapis.verify(getVerify("/subscription/api/v1/offer/list"));
    }

    @Test
    public void privilegesReachesKeyapis() {
        stubGet(keyapis, "/subscription/api/v1/privilege/list", fixture("/subscription/privileges.json"));

        assertThat(client.subscriptions().privileges()).hasSize(20);

        keyapis.verify(getVerify("/subscription/api/v1/privilege/list"));
    }

    @Test
    public void subscriptionsReachesKeyapis() {
        stubGet(keyapis, "/subscription/api/v1/subscription/list", fixture("/subscription/subscriptions.json"));

        assertThat(client.subscriptions().subscriptions()).hasSize(20);

        keyapis.verify(getVerify("/subscription/api/v1/subscription/list"));
    }

    @Test
    public void rfidsReachesHousehold() {
        stubGet(household, "/api/v5/devices/rfids", fixture("/keys/rfids.json"));

        Page<Rfid> rfids = client.keys().rfids();

        Rfid rfid = new Rfid(
                55577780, "Ключ мой ключ ", "0447222AEB6D55", List.of(77705511), List.of(20001),
                "master", "free",
                List.of(new Rfid.DeviceStatus(11558, 22233344, "loaded", Instant.parse("2025-11-10T16:20:09Z"))),
                List.of(9911),
                Instant.parse("2025-11-10T16:18:54Z"),
                Instant.parse("2025-11-10T19:20:08Z"));
        assertThat(rfids).isEqualTo(new Page<>(List.of(rfid), 1));
        household.verify(getVerify("/api/v5/devices/rfids"));
    }

    @Test
    public void createKeyReachesHousehold() {
        stubPost(household, "/api/v2/app/keys", fixture("/keys/key.json"));

        Key key = client.keys().createKey(1, "UID", "k");

        assertThat(key).isEqualTo(expectedKey());
        household.verify(postVerify("/api/v2/app/keys"));
    }

    @Test
    public void renameKeyReachesHousehold() {
        stubPatch(household, "/api/v2/app/keys/99", fixture("/keys/key.json"));

        Key key = client.keys().renameKey(99, "new name");

        assertThat(key).isEqualTo(expectedKey());
        household.verify(patchVerify("/api/v2/app/keys/99"));
    }

    @Test
    public void deleteRfidReachesHousehold() {
        stubDelete(household, "/api/v5/devices/rfids/98712");

        client.keys().deleteRfid(98712);

        household.verify(deleteVerify("/api/v5/devices/rfids/98712"));
    }

    private static Device expectedDevice() {
        return new Device(
                11155, 4567, "intercom", "qtech", "подъезд №1", "online", "sync_success",
                Instant.parse("2025-11-05T18:06:12Z"), null, 0,
                UUID.fromString("efe45ee2-8c3b-41a0-8b22-e2702db6087e"),
                "processing_error", "URL имеет неверное значение");
    }

    private static Key expectedKey() {
        return new Key(
                55552501, "My awesome key", "4457262AEB6D01",
                Instant.parse("2025-09-28T21:38:38.275328213Z"), null, 55554411, "tenant", "", null);
    }
}
