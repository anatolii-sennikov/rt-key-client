package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.model.EmergencyCode;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmergencyCodeDataTest {
    private static final Instant CREATED_AT = Instant.parse("2025-01-02T03:04:05Z");
    private static final Instant BEGIN_AT = Instant.parse("2025-01-03T00:00:00Z");
    private static final Instant EXPIRES_AT = Instant.parse("2026-01-02T03:04:05Z");
    private static final Instant FIRST_STATUS_CHANGED_AT = Instant.parse("2025-02-11T10:00:00Z");
    private static final Instant SECOND_STATUS_CHANGED_AT = Instant.parse("2025-02-12T11:00:00Z");
    private static final Instant THIRD_STATUS_CHANGED_AT = Instant.parse("2025-02-13T12:00:00Z");

    private static final EmergencyCode.User EXPECTED_OWNER = new EmergencyCode.User(
            7001, "Иван", "Иванович", "Иванов", "owner-login", "+74950000001", "owner@example.com");
    private static final EmergencyCode.User EXPECTED_CREATOR = new EmergencyCode.User(
            7002, "Пётр", "Петрович", "Петров", "creator-login", "+74950000002", "creator@example.com");

    @Test
    public void toModelMapsEveryField() {
        EmergencyCode code = EmergencyCodeData.toModel(full(statuses()));

        assertThat(code).isEqualTo(new EmergencyCode(
                88002,
                "7788",
                "emergency",
                "Тревожный код",
                new EmergencyCode.Flat(1234, "9"),
                new EmergencyCode.Company(4321),
                "tenant",
                EXPECTED_OWNER,
                "admin",
                EXPECTED_CREATOR,
                CREATED_AT,
                BEGIN_AT,
                EXPIRES_AT,
                List.of(
                        new EmergencyCode.Status(firstDevice(), FIRST_STATUS_CHANGED_AT, "active"),
                        new EmergencyCode.Status(secondDevice(), SECOND_STATUS_CHANGED_AT, "inactive")
                )
        ));
    }

    @Test
    public void toModelRebuildsOwnerAndCreatorAsModelUsers() {
        EmergencyCode code = EmergencyCodeData.toModel(full(statuses()));

        assertThat(code.owner()).isEqualTo(EXPECTED_OWNER);
        assertThat(code.creator()).isEqualTo(EXPECTED_CREATOR);
    }

    @Test
    public void toModelRebuildsEveryStatusDeviceWithItsOwnCompany() {
        EmergencyCode code = EmergencyCodeData.toModel(full(statuses()));

        assertThat(code.statuses()).containsExactly(
                new EmergencyCode.Status(firstDevice(), FIRST_STATUS_CHANGED_AT, "active"),
                new EmergencyCode.Status(secondDevice(), SECOND_STATUS_CHANGED_AT, "inactive")
        );
    }

    @Test
    public void toModelKeepsStatusOrder() {
        List<EmergencyCodeData.Status> threeStatuses = List.of(
                new EmergencyCodeData.Status(firstDeviceData(), FIRST_STATUS_CHANGED_AT, "active"),
                new EmergencyCodeData.Status(secondDeviceData(), SECOND_STATUS_CHANGED_AT, "inactive"),
                new EmergencyCodeData.Status(
                        device(9003, 5503, "gate", "Ворота 3", "7733", "300"),
                        THIRD_STATUS_CHANGED_AT,
                        "active"
                )
        );

        EmergencyCode code = EmergencyCodeData.toModel(full(threeStatuses));

        assertThat(code.statuses()).extracting(status -> status.device().id())
                .containsExactly(9001, 9002, 9003);
        assertThat(code.statuses()).extracting(EmergencyCode.Status::statusChangedAt)
                .containsExactly(FIRST_STATUS_CHANGED_AT, SECOND_STATUS_CHANGED_AT, THIRD_STATUS_CHANGED_AT);
    }

    @Test
    public void toModelKeepsNullableNestedObjectsNull() {
        EmergencyCodeData data = emergencyCodeData(null, null, null, null, null, null, null, List.of());

        EmergencyCode code = EmergencyCodeData.toModel(data);

        assertThat(code.title()).isNull();
        assertThat(code.flat()).isNull();
        assertThat(code.company()).isNull();
        assertThat(code.ownerType()).isNull();
        assertThat(code.owner()).isNull();
        assertThat(code.creator()).isNull();
        assertThat(code.beginAt()).isNull();
        assertThat(code.statuses()).isEmpty();
        assertThat(code.id()).isEqualTo(88002);
        assertThat(code.code()).isEqualTo("7788");
        assertThat(code.type()).isEqualTo("emergency");
        assertThat(code.creatorType()).isEqualTo("admin");
        assertThat(code.createdAt()).isEqualTo(CREATED_AT);
        assertThat(code.expiresAt()).isEqualTo(EXPIRES_AT);
    }

    @Test
    public void toModelKeepsNullableDeviceContactFieldsNull() {
        List<EmergencyCodeData.Status> statuses = List.of(new EmergencyCodeData.Status(
                device(9001, 5501, "intercom", "Подъезд 1", null, null),
                FIRST_STATUS_CHANGED_AT,
                "active"
        ));

        EmergencyCode code = EmergencyCodeData.toModel(full(statuses));

        EmergencyCode.Device device = code.statuses().get(0).device();
        assertThat(device.id()).isEqualTo(9001);
        assertThat(device.company()).isEqualTo(new EmergencyCode.Company(5501));
        assertThat(device.type()).isEqualTo("intercom");
        assertThat(device.title()).isEqualTo("Подъезд 1");
        assertThat(device.fullCode()).isNull();
        assertThat(device.callNumber()).isNull();
    }

    @Test
    public void toModelKeepsNullableUserFieldsNull() {
        EmergencyCodeData data = emergencyCodeData(
                "Тревожный код",
                new EmergencyCodeData.Flat(1234, "9"),
                new EmergencyCodeData.Company(4321),
                "tenant",
                new EmergencyCodeData.User(7001, null, null, null, null, null, null),
                creator(),
                BEGIN_AT,
                statuses()
        );

        EmergencyCode code = EmergencyCodeData.toModel(data);

        assertThat(code.owner())
                .isEqualTo(new EmergencyCode.User(7001, null, null, null, null, null, null));
        assertThat(code.creator()).isEqualTo(EXPECTED_CREATOR);
    }

    private static EmergencyCodeData full(List<EmergencyCodeData.Status> statuses) {
        return emergencyCodeData(
                "Тревожный код",
                new EmergencyCodeData.Flat(1234, "9"),
                new EmergencyCodeData.Company(4321),
                "tenant",
                owner(),
                creator(),
                BEGIN_AT,
                statuses
        );
    }

    private static EmergencyCodeData.User owner() {
        return new EmergencyCodeData.User(7001, "Иван", "Иванович", "Иванов",
                "owner-login", "+74950000001", "owner@example.com");
    }

    private static EmergencyCodeData.User creator() {
        return new EmergencyCodeData.User(7002, "Пётр", "Петрович", "Петров",
                "creator-login", "+74950000002", "creator@example.com");
    }

    private static List<EmergencyCodeData.Status> statuses() {
        return List.of(
                new EmergencyCodeData.Status(firstDeviceData(), FIRST_STATUS_CHANGED_AT, "active"),
                new EmergencyCodeData.Status(secondDeviceData(), SECOND_STATUS_CHANGED_AT, "inactive")
        );
    }

    private static EmergencyCodeData.Device firstDeviceData() {
        return device(9001, 5501, "intercom", "Подъезд 1", "9911", "100");
    }

    private static EmergencyCodeData.Device secondDeviceData() {
        return device(9002, 5502, "barrier", "Шлагбаум 2", "8822", "200");
    }

    private static EmergencyCode.Device firstDevice() {
        return new EmergencyCode.Device(9001, new EmergencyCode.Company(5501),
                "intercom", "Подъезд 1", "9911", "100");
    }

    private static EmergencyCode.Device secondDevice() {
        return new EmergencyCode.Device(9002, new EmergencyCode.Company(5502),
                "barrier", "Шлагбаум 2", "8822", "200");
    }

    private static EmergencyCodeData.Device device(Integer id, Integer companyId, String type, String title,
                                                   @Nullable String fullCode, @Nullable String callNumber) {
        return new EmergencyCodeData.Device(
                id,
                new EmergencyCodeData.Company(companyId),
                type,
                title,
                fullCode,
                callNumber
        );
    }

    private static EmergencyCodeData emergencyCodeData(@Nullable String title,
                                                      EmergencyCodeData.@Nullable Flat flat,
                                                      EmergencyCodeData.@Nullable Company company,
                                                      @Nullable String ownerType,
                                                      EmergencyCodeData.@Nullable User owner,
                                                      EmergencyCodeData.@Nullable User creator,
                                                      @Nullable Instant beginAt,
                                                      List<EmergencyCodeData.Status> statuses) {
        return new EmergencyCodeData(
                88002,
                "7788",
                "emergency",
                title,
                flat,
                company,
                ownerType,
                owner,
                "admin",
                creator,
                CREATED_AT,
                beginAt,
                EXPIRES_AT,
                statuses
        );
    }
}
