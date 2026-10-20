package io.github.anatoliisennikov.rtkey.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PrivilegeQueryTest {
    @Test
    public void limitMustBePositive() {
        assertThatThrownBy(() -> new PrivilegeQuery(0, List.of(), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("limit must be positive, got 0");
        assertThatThrownBy(() -> new PrivilegeQuery(-1, List.of(), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("limit must be positive, got -1");
    }

    @Test
    public void builderAcceptsCollections() {
        assertThat(PrivilegeQuery.builder()
                .flatNumbers(List.of("136"))
                .orpons(List.of("113156294"))
                .build())
                .isEqualTo(new PrivilegeQuery(null, List.of("136"), List.of("113156294")));
    }
}
