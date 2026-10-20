package io.github.anatoliisennikov.rtkey.internal.account;

import io.github.anatoliisennikov.rtkey.model.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class UserDataTest {
    private static final Instant CREATED_AT = Instant.parse("2024-10-01T09:00:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2025-05-06T07:08:09Z");
    private static final Instant ACTIVATED_AT = Instant.parse("2024-10-02T10:00:00Z");

    private static final Integer FIRST_BUILDING_ID = 10;
    private static final Integer FIRST_BUILDING_ORPON_ID = 100;
    private static final String FIRST_BUILDING_PLACE = "Москва";
    private static final String FIRST_BUILDING_STREET = "Тверская";
    private static final Integer SECOND_BUILDING_ID = 20;
    private static final Integer SECOND_BUILDING_ORPON_ID = 200;
    private static final String SECOND_BUILDING_PLACE = "Санкт-Петербург";
    private static final String SECOND_BUILDING_STREET = "Невский";

    @Test
    public void toModelMapsUserAndCompany() {
        User user = UserData.toModel(userData(twoBuildings()));

        assertThat(user).isEqualTo(new User(
                7001,
                "user-login",
                "+74950000001",
                "active",
                true,
                false,
                new User.Company(4321, "УК Ромашка"),
                expectedFlats()
        ));
    }

    @Test
    public void toModelFlattensBuildingsEntrancesAndFloorsIntoFlats() {
        User user = UserData.toModel(userData(twoBuildings()));

        assertThat(user.flats()).containsExactlyElementsOf(expectedFlats());
    }

    @Test
    public void toModelDenormalisesBuildingIdentityOntoEveryFlat() {
        User user = UserData.toModel(userData(twoBuildings()));

        assertThat(user.flats()).extracting(flat -> flat.buildingId())
                .containsExactly(
                        FIRST_BUILDING_ID, FIRST_BUILDING_ID, FIRST_BUILDING_ID, FIRST_BUILDING_ID,
                        FIRST_BUILDING_ID, SECOND_BUILDING_ID
                );
        assertThat(user.flats()).extracting(flat -> flat.orponId())
                .containsExactly(
                        FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_ORPON_ID,
                        FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_ORPON_ID, SECOND_BUILDING_ORPON_ID
                );
        assertThat(user.flats()).extracting(flat -> flat.buildingPlace())
                .containsExactly(
                        FIRST_BUILDING_PLACE, FIRST_BUILDING_PLACE, FIRST_BUILDING_PLACE,
                        FIRST_BUILDING_PLACE, FIRST_BUILDING_PLACE, SECOND_BUILDING_PLACE
                );
        assertThat(user.flats()).extracting(flat -> flat.buildingStreet())
                .containsExactly(
                        FIRST_BUILDING_STREET, FIRST_BUILDING_STREET, FIRST_BUILDING_STREET,
                        FIRST_BUILDING_STREET, FIRST_BUILDING_STREET, SECOND_BUILDING_STREET
                );
    }

    @Test
    public void toModelSkipsEmptyLevelsOfTheTree() {
        List<Building> buildings = List.of(
                building(FIRST_BUILDING_ID, FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_PLACE, FIRST_BUILDING_STREET,
                        List.of(
                                entrance("e1", "Подъезд 1", List.of(floor("f1", "Этаж 1", List.of(flat(101))))),
                                entrance("e2", "Подъезд 2", List.of()),
                                entrance("e3", "Подъезд 3", List.of(floor("f3", "Этаж 3", List.of())))
                        )),
                building(SECOND_BUILDING_ID, SECOND_BUILDING_ORPON_ID, SECOND_BUILDING_PLACE, SECOND_BUILDING_STREET,
                        List.of())
        );

        User user = UserData.toModel(userData(buildings));

        assertThat(user.flats()).containsExactly(expectedFlat(101, FIRST_BUILDING_ID, FIRST_BUILDING_ORPON_ID,
                FIRST_BUILDING_PLACE, FIRST_BUILDING_STREET, "e1", "Подъезд 1", "f1", "Этаж 1"));
    }

    @Test
    public void toModelYieldsNoFlatsWithoutBuildings() {
        User user = UserData.toModel(userData(List.of()));

        assertThat(user.flats()).isEmpty();
        assertThat(user.userId()).isEqualTo(7001);
        assertThat(user.company()).isEqualTo(new User.Company(4321, "УК Ромашка"));
    }

    @Test
    public void toModelKeepsVerificationFlagsApart() {
        User user = UserData.toModel(userData(twoBuildings()));

        assertThat(user.phoneNumberVerified()).isTrue();
        assertThat(user.emailVerified()).isFalse();
    }

    private static List<Building> twoBuildings() {
        return List.of(
                building(FIRST_BUILDING_ID, FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_PLACE, FIRST_BUILDING_STREET,
                        List.of(
                                entrance("e1", "Подъезд 1", List.of(
                                        floor("f1", "Этаж 1", List.of(flat(101), flat(102))),
                                        floor("f2", "Этаж 2", List.of(flat(201)))
                                )),
                                entrance("e2", "Подъезд 2", List.of(
                                        floor("f3", "Этаж 3", List.of(flat(301), flat(302)))
                                ))
                        )),
                building(SECOND_BUILDING_ID, SECOND_BUILDING_ORPON_ID, SECOND_BUILDING_PLACE, SECOND_BUILDING_STREET,
                        List.of(entrance("e3", "Подъезд 3", List.of(floor("f4", "Этаж 4", List.of(flat(401))))))
                )
        );
    }

    private static List<io.github.anatoliisennikov.rtkey.model.Flat> expectedFlats() {
        return List.of(
                expectedFlat(101, FIRST_BUILDING_ID, FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_PLACE,
                        FIRST_BUILDING_STREET, "e1", "Подъезд 1", "f1", "Этаж 1"),
                expectedFlat(102, FIRST_BUILDING_ID, FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_PLACE,
                        FIRST_BUILDING_STREET, "e1", "Подъезд 1", "f1", "Этаж 1"),
                expectedFlat(201, FIRST_BUILDING_ID, FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_PLACE,
                        FIRST_BUILDING_STREET, "e1", "Подъезд 1", "f2", "Этаж 2"),
                expectedFlat(301, FIRST_BUILDING_ID, FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_PLACE,
                        FIRST_BUILDING_STREET, "e2", "Подъезд 2", "f3", "Этаж 3"),
                expectedFlat(302, FIRST_BUILDING_ID, FIRST_BUILDING_ORPON_ID, FIRST_BUILDING_PLACE,
                        FIRST_BUILDING_STREET, "e2", "Подъезд 2", "f3", "Этаж 3"),
                expectedFlat(401, SECOND_BUILDING_ID, SECOND_BUILDING_ORPON_ID, SECOND_BUILDING_PLACE,
                        SECOND_BUILDING_STREET, "e3", "Подъезд 3", "f4", "Этаж 4")
        );
    }

    private static io.github.anatoliisennikov.rtkey.model.Flat expectedFlat(
            Integer id, Integer buildingId, Integer orponId, String place, String street,
            String entranceId, String entranceName, String floorId, String floorName) {
        return new io.github.anatoliisennikov.rtkey.model.Flat(
                id, "Кв. " + id, buildingId, orponId, place, street, entranceId, entranceName, floorId, floorName);
    }

    private static Building building(Integer id, Integer orponId, String place, String street,
                                    List<Entrance> entrances) {
        return new Building(id, 1, 2, place, street, "1А", 3, orponId, 4321, 180, "77000000000", entrances);
    }

    private static Entrance entrance(String id, String name, List<Floor> floors) {
        return new Entrance(id, name, floors);
    }

    private static Floor floor(String id, String name, List<Flat> flats) {
        return new Floor(id, name, flats);
    }

    private static Flat flat(Integer id) {
        return new Flat(
                id,
                4321,
                "Кв. " + id,
                42,
                "account-" + id,
                List.of(),
                List.of(),
                new UserSettings(true, false, true),
                "private",
                new RoomPermissions(true, true, true, true, true, true, true, true, true),
                List.of("open_door"),
                true
        );
    }

    private static UserData userData(List<Building> buildings) {
        return new UserData(
                7001,
                "tenant",
                "user-login",
                "+74950000001",
                "user@example.com",
                "invoice@example.com",
                "active",
                false,
                "sso-1",
                "mos-sso-1",
                11,
                true,
                "ory-1",
                14,
                15,
                16,
                17,
                new Company(4321, 43, "УК Ромашка", 180, "Ссылка", "ios://deep", "https://apps.apple.com/rt",
                        "ru.rt.key", "com.rt.key", "https://key.rt.ru", "Описание ссылки"),
                "Описание пользователя",
                20,
                "admin",
                true,
                false,
                CREATED_AT,
                null,
                UPDATED_AT,
                ACTIVATED_AT,
                buildings,
                "https://env.example",
                "owner-30",
                "full"
        );
    }
}
