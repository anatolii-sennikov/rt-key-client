package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.model.Privilege;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PrivilegeDataTest {

    @Test
    public void toModelMapsEveryField() {
        PrivilegeData data = new PrivilegeData("orpon-1", "77", "rfid", "active");

        Privilege privilege = PrivilegeData.toModel(data);

        assertThat(privilege).isEqualTo(new Privilege("orpon-1", "77", "rfid", "active"));
    }

    @Test
    public void toModelKeepsTypeAndStateTypeApart() {
        PrivilegeData data = new PrivilegeData("orpon-1", "77", "rfid", "active");

        Privilege privilege = PrivilegeData.toModel(data);

        assertThat(privilege.type()).isEqualTo("rfid");
        assertThat(privilege.stateType()).isEqualTo("active");
    }

    @Test
    public void toModelKeepsOrponAndFlatNumberApart() {
        PrivilegeData data = new PrivilegeData("orpon-1", "77", "rfid", "active");

        Privilege privilege = PrivilegeData.toModel(data);

        assertThat(privilege.orpon()).isEqualTo("orpon-1");
        assertThat(privilege.flatNumber()).isEqualTo("77");
    }

    @Test
    public void toModelKeepsCyrillicValues() {
        PrivilegeData data = new PrivilegeData("орпон-1", "77А", "ключ", "заблокирован");

        Privilege privilege = PrivilegeData.toModel(data);

        assertThat(privilege).isEqualTo(new Privilege("орпон-1", "77А", "ключ", "заблокирован"));
    }
}
