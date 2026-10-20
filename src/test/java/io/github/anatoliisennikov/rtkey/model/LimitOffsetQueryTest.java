package io.github.anatoliisennikov.rtkey.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LimitOffsetQueryTest {
    private static final String LIMIT_ERROR_MESSAGE = "limit must be positive, got ";

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void limitMustBePositive(int limit) {
        assertThatThrownBy(() -> new LimitOffsetQuery(limit, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(LIMIT_ERROR_MESSAGE + limit);
    }

    @Test
    public void offsetMustNotBeNegative() {
        assertThatThrownBy(() -> new LimitOffsetQuery(null, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("offset must not be negative, got -1");
    }
}
