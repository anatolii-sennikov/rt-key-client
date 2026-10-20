package io.github.anatoliisennikov.rtkey.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EventsQueryTest {
    private static final String LIMIT_ERROR_MESSAGE = "limit must be positive, got ";

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void limitMustBePositive(int limit) {
        assertThatThrownBy(() -> new EventsQuery(limit, null, List.of(), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(LIMIT_ERROR_MESSAGE + limit);
    }

    @Test
    public void offsetMustNotBeNegative() {
        assertThatThrownBy(() -> new EventsQuery(null, -1, List.of(), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("offset must not be negative, got -1");
    }

    @Test
    public void builderAcceptsCollections() {
        assertThat(EventsQuery.builder()
                .deviceIds(List.of("123"))
                .eventTypes(List.of(EventTypes.API_OPEN_REMOTE))
                .build())
                .isEqualTo(new EventsQuery(null, null, List.of("123"), List.of(EventTypes.API_OPEN_REMOTE)));
    }
}
