package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.model.Code;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CodeDataTest {
    private static final Instant CREATED_AT = Instant.parse("2025-01-02T03:04:05Z");
    private static final Instant BEGIN_AT = Instant.parse("2025-01-03T00:00:00Z");
    private static final Instant EXPIRES_AT = Instant.parse("2026-01-02T03:04:05Z");
    private static final Instant FIRST_STATUS_CHANGED_AT = Instant.parse("2025-02-11T10:00:00Z");
    private static final Instant SECOND_STATUS_CHANGED_AT = Instant.parse("2025-02-12T11:00:00Z");

    @Test
    public void toModelMapsEveryField() {
        Code code = CodeData.toModel(codeData(
                "Код для няни",
                new CodeData.Flat(1234, "9"),
                new CodeData.Company(4321),
                new CodeData.Owner(7001, "owner@example.com", "owner-login", "+74950000001", "active"),
                new CodeData.Creator(7002, "creator@example.com", "creator-login", "+74950000002", "blocked"),
                EXPIRES_AT,
                statuses()
        ));

        assertThat(code).isEqualTo(new Code(
                88001,
                "9911",
                "permanent",
                "Код для няни",
                1234,
                4321,
                "tenant",
                7001,
                "owner-login",
                "+74950000001",
                "active",
                7002,
                "creator-login",
                "+74950000002",
                "blocked",
                List.of(
                        new Code.Status(9001, 5501, "Подъезд 1", "active"),
                        new Code.Status(9002, 5502, "Шлагбаум 2", "inactive")
                ),
                CREATED_AT,
                EXPIRES_AT
        ));
    }

    @Test
    public void toModelFlattensOwnerAndCreatorSeparately() {
        Code code = CodeData.toModel(codeData(
                "Код для няни",
                new CodeData.Flat(1234, "9"),
                new CodeData.Company(4321),
                new CodeData.Owner(7001, "owner@example.com", "owner-login", "+74950000001", "active"),
                new CodeData.Creator(7002, "creator@example.com", "creator-login", "+74950000002", "blocked"),
                EXPIRES_AT,
                statuses()
        ));

        assertThat(code.ownerId()).isEqualTo(7001);
        assertThat(code.ownerLogin()).isEqualTo("owner-login");
        assertThat(code.ownerPhoneNumber()).isEqualTo("+74950000001");
        assertThat(code.ownerStatus()).isEqualTo("active");
        assertThat(code.creatorId()).isEqualTo(7002);
        assertThat(code.creatorLogin()).isEqualTo("creator-login");
        assertThat(code.creatorPhoneNumber()).isEqualTo("+74950000002");
        assertThat(code.creatorStatus()).isEqualTo("blocked");
    }

    @Test
    public void toModelNarrowsEachStatusToItsDeviceFields() {
        Code code = CodeData.toModel(codeData(
                "Код для няни",
                new CodeData.Flat(1234, "9"),
                new CodeData.Company(4321),
                new CodeData.Owner(7001, "owner@example.com", "owner-login", "+74950000001", "active"),
                new CodeData.Creator(7002, "creator@example.com", "creator-login", "+74950000002", "blocked"),
                EXPIRES_AT,
                statuses()
        ));

        assertThat(code.statuses()).containsExactly(
                new Code.Status(9001, 5501, "Подъезд 1", "active"),
                new Code.Status(9002, 5502, "Шлагбаум 2", "inactive")
        );
    }

    @Test
    public void toModelKeepsAbsentNestedObjectsNull() {
        Code code = CodeData.toModel(codeData(null, null, null, null, null, null, List.of()));

        assertThat(code.title()).isNull();
        assertThat(code.flatId()).isNull();
        assertThat(code.companyId()).isNull();
        assertThat(code.ownerId()).isNull();
        assertThat(code.ownerLogin()).isNull();
        assertThat(code.ownerPhoneNumber()).isNull();
        assertThat(code.ownerStatus()).isNull();
        assertThat(code.creatorId()).isNull();
        assertThat(code.creatorLogin()).isNull();
        assertThat(code.creatorPhoneNumber()).isNull();
        assertThat(code.creatorStatus()).isNull();
        assertThat(code.expiresAt()).isNull();
        assertThat(code.statuses()).isEmpty();
    }

    @Test
    public void toModelIgnoresOwnerAndCreatorEmails() {
        Code withEmails = CodeData.toModel(codeData(
                "Код для няни",
                new CodeData.Flat(1234, "9"),
                new CodeData.Company(4321),
                new CodeData.Owner(7001, "owner@example.com", "owner-login", "+74950000001", "active"),
                new CodeData.Creator(7002, "creator@example.com", "creator-login", "+74950000002", "blocked"),
                EXPIRES_AT,
                statuses()
        ));
        Code withOtherEmails = CodeData.toModel(codeData(
                "Код для няни",
                new CodeData.Flat(1234, "9"),
                new CodeData.Company(4321),
                new CodeData.Owner(7001, "another-owner@example.com", "owner-login", "+74950000001", "active"),
                new CodeData.Creator(7002, "another-creator@example.com", "creator-login", "+74950000002", "blocked"),
                EXPIRES_AT,
                statuses()
        ));

        assertThat(withEmails).isEqualTo(withOtherEmails);
        assertThat(withEmails.createdAt()).isEqualTo(CREATED_AT);
        assertThat(withEmails.expiresAt()).isEqualTo(EXPIRES_AT);
    }

    private static List<CodeStatus> statuses() {
        return List.of(
                new CodeStatus(
                        new StatusesDevice(9001, new DeviceCompany(5501), "intercom", "Подъезд 1", "9911", "100"),
                        FIRST_STATUS_CHANGED_AT,
                        "active"
                ),
                new CodeStatus(
                        new StatusesDevice(9002, new DeviceCompany(5502), "barrier", "Шлагбаум 2", "8822", "200"),
                        SECOND_STATUS_CHANGED_AT,
                        "inactive"
                )
        );
    }

    private static CodeData codeData(@Nullable String title,
                                     CodeData.@Nullable Flat flat,
                                     CodeData.@Nullable Company company,
                                     CodeData.@Nullable Owner owner,
                                     CodeData.@Nullable Creator creator,
                                     @Nullable Instant expiresAt,
                                     List<CodeStatus> statuses) {
        return new CodeData(
                88001,
                "9911",
                "permanent",
                title,
                flat,
                company,
                "tenant",
                owner,
                "admin",
                creator,
                CREATED_AT,
                BEGIN_AT,
                expiresAt,
                statuses
        );
    }
}
