package io.github.anatoliisennikov.rtkey.internal.transport;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseTest {
    @Test
    public void notSuccess199() {
        Response response = new Response(199, "something");
        assertThat(response.success()).isFalse();
    }

    @Test
    public void success200() {
        Response response = new Response(200, "something");
        assertThat(response.success()).isTrue();
    }

    @Test
    public void success299() {
        Response response = new Response(299, "something");
        assertThat(response.success()).isTrue();
    }

    @Test
    public void notSuccess300() {
        Response response = new Response(300, "something");
        assertThat(response.success()).isFalse();
    }
}
