package io.github.anatoliisennikov.rtkey.exception;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RtKeyExceptionTest {

    @Test
    public void requestIdRetained() {
        RtKeyApiException ex = new RtKeyApiException(400, "E1", "req-42", "boom", List.of());
        assertThat(ex.getRequestId()).isEqualTo("req-42");
    }

    @Test
    public void requestIdNullWhenAbsent() {
        RtKeyContractException ex = new RtKeyContractException("bad payload");
        assertThat(ex.getRequestId()).isNull();
    }

    @Test
    public void causePreserved() {
        IOException cause = new IOException("boom");
        RtKeyNetworkException ex = new RtKeyNetworkException("wrap", cause);
        assertThat(ex.getCause()).isSameAs(cause);
        assertThat(ex.getMessage()).isEqualTo("wrap");
    }

    @Test
    public void requestIdNotInMessage() {
        RtKeyApiException ex = new RtKeyApiException(400, null, "req-42", "boom", List.of());
        assertThat(ex.getMessage()).isEqualTo("boom").doesNotContain("req-42");
    }
}
