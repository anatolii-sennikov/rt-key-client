package io.github.anatoliisennikov.rtkey;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RtKeyAuthTest {

    @Test
    public void buildReturnsAuth() {
        try (RtKeyAuth auth = RtKeyAuth.builder()
                .deviceId(UUID.randomUUID())
                .build()) {
            assertThat(auth).isNotNull();
        }
    }

    @Test
    public void deviceIdIsRequired() {
        assertThatThrownBy(() -> RtKeyAuth.builder().build())
                .isInstanceOf(NullPointerException.class)
                .hasMessage("deviceId is required");
    }

    @Test
    public void baseUriRejectsNonKeyapisHost() {
        assertThatThrownBy(() -> RtKeyAuth.builder().baseUri(Host.HOUSEHOLD, URI.create("http://localhost:8080")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RtKeyAuth overrides only KEYAPIS, got HOUSEHOLD");
    }

    @Test
    public void buildAcceptsKeyapisBaseUriOverride() {
        try (RtKeyAuth auth = RtKeyAuth.builder()
                .deviceId(UUID.randomUUID())
                .baseUri(Host.KEYAPIS, URI.create("http://localhost:8080"))
                .build()) {
            assertThat(auth).isNotNull();
        }
    }

    @Test
    public void connectTimeoutRejectsZero() {
        assertThatThrownBy(() -> RtKeyAuth.builder().deviceId(UUID.randomUUID()).connectTimeout(Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("connectTimeout must be positive");
    }

    @Test
    public void httpClientWithConnectTimeoutThrows() {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            RtKeyAuth.Builder builder = RtKeyAuth.builder()
                    .deviceId(UUID.randomUUID())
                    .httpClient(httpClient)
                    .connectTimeout(Duration.ofSeconds(1));

            assertThatThrownBy(builder::build)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("connectTimeout cannot be combined with httpClient: "
                            + "set the connect timeout on your own HttpClient");
        }
    }
}
