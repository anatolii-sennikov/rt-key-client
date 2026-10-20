package io.github.anatoliisennikov.rtkey.exception;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RtKeyApiExceptionTest {

    @Test
    public void serverMessageWins() {
        RtKeyApiException ex = new RtKeyApiException(400, "E1", "req-1", "Bad request", List.of());
        assertThat(ex.getMessage()).isEqualTo("Bad request");
    }

    @Test
    public void serverMessageWithDetails() {
        RtKeyApiException ex = new RtKeyApiException(400, "E1", "req-1", "Bad request", List.of("a", "b"));
        assertThat(ex.getMessage()).isEqualTo("Bad request: a; b");
    }

    @Test
    public void detailsOnly() {
        RtKeyApiException ex = new RtKeyApiException(400, "E1", "req-1", null, List.of("a", "b"));
        assertThat(ex.getMessage()).isEqualTo("a; b");
    }

    @Test
    public void fallbackWithServerCode() {
        RtKeyApiException ex = new RtKeyApiException(400, "E1", null, null, List.of());
        assertThat(ex.getMessage()).isEqualTo("API error E1 (HTTP 400)");
    }

    @Test
    public void fallbackWithoutServerCode() {
        RtKeyApiException ex = new RtKeyApiException(500, null, null, null, List.of());
        assertThat(ex.getMessage()).isEqualTo("API error (HTTP 500)");
    }

    @Test
    public void getters() {
        RtKeyApiException ex = new RtKeyApiException(422, "E2", "req-9", "msg", List.of("x"));
        assertThat(ex.getStatus()).isEqualTo(422);
        assertThat(ex.getServerCode()).isEqualTo("E2");
        assertThat(ex.getRequestId()).isEqualTo("req-9");
        assertThat(ex.getDescription()).containsExactly("x");
    }

    @Test
    public void serverCodeMayBeNull() {
        RtKeyApiException ex = new RtKeyApiException(400, null, null, "msg", List.of());
        assertThat(ex.getServerCode()).isNull();
    }

    @Test
    public void descriptionDefensiveCopy() {
        List<String> details = new ArrayList<>();
        details.add("x");
        RtKeyApiException ex = new RtKeyApiException(400, null, null, null, details);
        details.add("y");
        assertThat(ex.getDescription()).containsExactly("x");
    }

    @Test
    public void descriptionImmutable() {
        RtKeyApiException ex = new RtKeyApiException(400, null, null, null, List.of("x"));
        assertThatThrownBy(() -> ex.getDescription().add("y"))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
