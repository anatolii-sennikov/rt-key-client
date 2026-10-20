package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.internal.account.AccountServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.camera.CameraMediaServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.camera.CameraServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.code.CodeServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.device.DeviceServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.events.EventServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.keys.KeysServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.subscription.SubscriptionServiceImpl;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RtKeyClientTest {

    @Test
    public void buildReturnsClientWithServices() {
        try (RtKeyClient client = RtKeyClient.builder("token").build()) {
            assertThat(client.account()).isInstanceOf(AccountServiceImpl.class);
            assertThat(client.devices()).isInstanceOf(DeviceServiceImpl.class);
            assertThat(client.codes()).isInstanceOf(CodeServiceImpl.class);
            assertThat(client.keys()).isInstanceOf(KeysServiceImpl.class);
            assertThat(client.cameras()).isInstanceOf(CameraServiceImpl.class);
            assertThat(client.cameraMedia()).isInstanceOf(CameraMediaServiceImpl.class);
            assertThat(client.events()).isInstanceOf(EventServiceImpl.class);
            assertThat(client.subscriptions()).isInstanceOf(SubscriptionServiceImpl.class);
        }
    }

    @Test
    public void buildAcceptsBaseUriOverrideForAnyHost() {
        try (RtKeyClient client = RtKeyClient.builder("token")
                .baseUri(Host.HOUSEHOLD, URI.create("http://localhost:8080"))
                .baseUri(Host.KEYAPIS, URI.create("http://localhost:8081"))
                .baseUri(Host.EVENTS, URI.create("http://localhost:8082"))
                .build()) {
            assertThat(client).isNotNull();
        }
    }

    @Test
    public void connectTimeoutRejectsZero() {
        assertThatThrownBy(() -> RtKeyClient.builder("token").connectTimeout(Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("connectTimeout must be positive");
    }

    @Test
    public void connectTimeoutRejectsNegative() {
        assertThatThrownBy(() -> RtKeyClient.builder("token").connectTimeout(Duration.ofSeconds(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("connectTimeout must be positive");
    }

    @Test
    public void httpClientWithConnectTimeoutThrows() {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            RtKeyClient.Builder builder = RtKeyClient.builder("token")
                    .httpClient(httpClient)
                    .connectTimeout(Duration.ofSeconds(1));

            assertThatThrownBy(builder::build)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("connectTimeout cannot be combined with httpClient: "
                            + "set the connect timeout on your own HttpClient");
        }
    }
}
