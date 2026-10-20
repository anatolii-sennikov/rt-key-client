package io.github.anatoliisennikov.rtkey.exception;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class RtKeyNetworkExceptionTest {

    @Test
    public void messageOnly() {
        RtKeyNetworkException ex = new RtKeyNetworkException("HTTP call failed");
        assertThat(ex.getMessage()).isEqualTo("HTTP call failed");
        assertThat(ex.getCause()).isNull();
        assertThat(ex.getRequestId()).isNull();
    }

    @Test
    public void messageAndCause() {
        IOException cause = new IOException("connection reset");
        RtKeyNetworkException ex = new RtKeyNetworkException("HTTP call failed", cause);
        assertThat(ex.getMessage()).isEqualTo("HTTP call failed");
        assertThat(ex.getCause()).isSameAs(cause);
    }
}
