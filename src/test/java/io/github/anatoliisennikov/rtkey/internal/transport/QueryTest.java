package io.github.anatoliisennikov.rtkey.internal.transport;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTest {
    @Test
    public void empty() {
        Query query = Query.empty();
        assertThat(query.isEmpty()).isTrue();
        assertThat(query.encoded().isEmpty()).isTrue();
    }

    @Test
    public void encodedSinglePair() {
        Query query = Query.of("key", "value");
        assertThat(query.isEmpty()).isFalse();
        assertThat(query.encoded()).isEqualTo("key=value");
    }

    @Test
    public void encodedRepeatedAndMultipleKeys() {
        Query query = Query.of("a", "1").add("a", "2").add("b", "3");
        assertThat(query.isEmpty()).isFalse();
        assertThat(query.encoded()).isEqualTo("a=1&a=2&b=3");
    }

    @Test
    public void encodedAddToEmptyQuery() {
        Query query = Query.empty().add("a", "1");
        assertThat(query.isEmpty()).isFalse();
        assertThat(query.encoded()).isEqualTo("a=1");
    }

    @Test
    public void addDoesNotMutateSourceQuery() {
        Query query = Query.empty();
        Query secondQuery = query.add("a", "1");
        assertThat(query.isEmpty()).isTrue();
        assertThat(secondQuery.encoded()).isEqualTo("a=1");
    }

    @Test
    public void encodedEscapesNonAsciiValue() {
        Query query = Query.of("a", "подъезд");

        assertThat(query.isEmpty()).isFalse();
        assertThat(query.encoded()).isEqualTo("a=%D0%BF%D0%BE%D0%B4%D1%8A%D0%B5%D0%B7%D0%B4");
    }

    @Test
    public void encodedEscapesSpaceAsPercent20() {
        Query query = Query.of("a", "подъезд 12");

        assertThat(query.isEmpty()).isFalse();
        assertThat(query.encoded()).isEqualTo("a=%D0%BF%D0%BE%D0%B4%D1%8A%D0%B5%D0%B7%D0%B4%2012");
    }

    @Test
    public void plusInValueIsEscaped() {
        assertThat(Query.of("q", "1+2").encoded()).isEqualTo("q=1%2B2");
    }

    @Test
    public void equalsAndHashCode() {
        assertThat(Query.of("a", "1")).isEqualTo(Query.of("a", "1"));
        assertThat(Query.of("a", "1")).hasSameHashCodeAs(Query.of("a", "1"));
        assertThat(Query.of("a", "1")).isNotEqualTo(Query.of("a", "2"));
    }

    @Test
    public void toStringMasksValues() {
        assertThat(Query.of("token", "secret").toString()).doesNotContain("secret");
    }
}
