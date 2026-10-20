package io.github.anatoliisennikov.rtkey.internal.transport;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class RedactedTest {

    @Test
    public void noQueryKeepsUri() {
        assertThat(Redacted.redact(URI.create("https://h/p"))).isEqualTo("https://h/p");
    }

    @Test
    public void emptyQueryNoQuestionMark() {
        assertThat(Redacted.redact(URI.create("https://h/p?"))).isEqualTo("https://h/p");
    }

    @Test
    public void removesOnlyTokenPair() {
        assertThat(Redacted.redact(URI.create("https://h/p?token=s&a=1&b=2")))
                .isEqualTo("https://h/p?a=1&b=2");
    }

    @Test
    public void tokenOnlyDropsWholeQuery() {
        assertThat(Redacted.redact(URI.create("https://h/p?token=s"))).isEqualTo("https://h/p");
    }

    @Test
    public void isCaseSensitive() {
        assertThat(Redacted.redact(URI.create("https://h/p?Token=s&a=1")))
                .isEqualTo("https://h/p?Token=s&a=1");
    }

    @Test
    public void keyWithoutEqualsKept() {
        assertThat(Redacted.redact(URI.create("https://h/p?flag&a=1")))
                .isEqualTo("https://h/p?flag&a=1");
    }

    @Test
    public void valueWithEqualsKeptWhole() {
        assertThat(Redacted.redact(URI.create("https://h/p?a=b=c"))).isEqualTo("https://h/p?a=b=c");
    }
}
