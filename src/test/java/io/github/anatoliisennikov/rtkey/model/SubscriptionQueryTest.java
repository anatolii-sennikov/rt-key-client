package io.github.anatoliisennikov.rtkey.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SubscriptionQueryTest {
    @Test
    public void limitMustBePositive() {
        assertThatThrownBy(() -> new SubscriptionQuery(0, List.of(), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("limit must be positive, got 0");
        assertThatThrownBy(() -> new SubscriptionQuery(-1, List.of(), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("limit must be positive, got -1");
    }

    @Test
    public void builderAcceptsCollections() {
        assertThat(SubscriptionQuery.builder()
                .flatNumbers(List.of("136"))
                .orpons(List.of("113156294"))
                .build())
                .isEqualTo(new SubscriptionQuery(null, List.of("136"), List.of("113156294")));
    }
}
