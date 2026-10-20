package io.github.anatoliisennikov.rtkey.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RtKeyContractExceptionTest {

    @Test
    public void messageOnly() {
        RtKeyContractException ex = new RtKeyContractException("bad payload");
        assertThat(ex.getMessage()).isEqualTo("bad payload");
        assertThat(ex.getCause()).isNull();
        assertThat(ex.getRequestId()).isNull();
    }

    @Test
    public void messageAndCause() {
        RuntimeException cause = new RuntimeException("root");
        RtKeyContractException ex = new RtKeyContractException("bad payload", cause);
        assertThat(ex.getMessage()).isEqualTo("bad payload");
        assertThat(ex.getCause()).isSameAs(cause);
    }
}
