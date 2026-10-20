package io.github.anatoliisennikov.rtkey.internal.transport;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BodyTest {

    @Test
    public void jsonContentType() {
        Body body = Body.json("{\"key\":\"value\"}");
        String contentType = body.contentType();
        assertThat(contentType).isEqualTo("application/json");
    }

    @Test
    public void identityContentType() {
        Body body = Body.identity("{\"key\":\"value\"}");
        String contentType = body.contentType();
        assertThat(contentType).isEqualTo("text/plain;charset=UTF-8");
    }

    @Test
    public void identityPredicate() {
        assertThat(Body.identity("x").identity()).isTrue();
        assertThat(Body.json("x").identity()).isFalse();
    }

    @Test
    public void toStringMasksPayload() {
        assertThat(Body.json("secret-token").toString()).doesNotContain("secret-token");
    }
}
