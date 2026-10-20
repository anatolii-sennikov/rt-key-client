package io.github.anatoliisennikov.rtkey.internal.transport;

import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HeadersTest {

    @Test
    public void emptyIsEmpty() {
        Headers headers = Headers.empty();
        assertThat(headers.isEmpty()).isTrue();
        assertThat(headers.entries()).isEmpty();
    }

    @Test
    public void addIsImmutable() {
        Headers first = Headers.of("A", "1");
        Headers second = first.add("B", "2");
        assertThat(first.entries()).hasSize(1);
        assertThat(second.entries()).hasSize(2);
    }

    @Test
    public void bearerFormatsAuthorization() {
        Headers headers = Headers.bearer(() -> "tok");
        assertThat(headers.entries()).containsExactly(new Headers.Entry("Authorization", "Bearer tok"));
    }

    @Test
    public void bearerRejectsNullToken() {
        assertThatThrownBy(() -> Headers.bearer(() -> null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("tokenSupplier returned null/blank token");
    }

    @Test
    public void bearerRejectsBlankToken() {
        assertThatThrownBy(() -> Headers.bearer(() -> "   "))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("tokenSupplier returned null/blank token");
    }

    @Test
    public void bearerEvaluatesSupplierOnce() {
        AtomicInteger calls = new AtomicInteger();
        Headers.bearer(() -> {
            calls.incrementAndGet();
            return "tok";
        });
        assertThat(calls.get()).isEqualTo(1);
    }

    @Test
    public void deviceIdFormatsHeader() {
        UUID deviceId = UUID.fromString("11111111-2222-3333-4444-555555555555");
        assertThat(Headers.deviceId(deviceId).entries())
                .containsExactly(new Headers.Entry("X-Device-Id", deviceId.toString()));
    }

    @Test
    public void entriesAreImmutable() {
        Headers headers = Headers.of("A", "1");
        assertThatThrownBy(() -> headers.entries().add(new Headers.Entry("B", "2")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void equalsAndHashCode() {
        assertThat(Headers.of("A", "1")).isEqualTo(Headers.of("A", "1"));
        assertThat(Headers.of("A", "1")).hasSameHashCodeAs(Headers.of("A", "1"));
        assertThat(Headers.of("A", "1")).isNotEqualTo(Headers.of("A", "2"));
        assertThat(Headers.empty().add("A", "1").add("B", "2"))
                .isNotEqualTo(Headers.empty().add("B", "2").add("A", "1"));
    }

    @Test
    public void toStringMasksValues() {
        assertThat(Headers.bearer(() -> "secret").toString()).doesNotContain("secret");
    }
}
