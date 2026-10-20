package io.github.anatoliisennikov.rtkey.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OfferQueryTest {
    private static final String LIMIT_ERROR_MESSAGE = "limit must be positive, got ";

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void limitMustBePositive(int limit) {
        assertThatThrownBy(() -> new OfferQuery(limit, List.of(), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(LIMIT_ERROR_MESSAGE + limit);
    }

    @Test
    public void builderAcceptsCollections() {
        assertThat(OfferQuery.builder()
                .orpons(List.of("113156294"))
                .types(List.of(OfferType.SHARED, OfferType.RTK))
                .build())
                .isEqualTo(new OfferQuery(null, List.of("113156294"), List.of(OfferType.SHARED, OfferType.RTK)));
    }
}
