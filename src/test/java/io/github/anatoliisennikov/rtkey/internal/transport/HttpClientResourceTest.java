package io.github.anatoliisennikov.rtkey.internal.transport;

import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class HttpClientResourceTest {

    @Test
    public void clientReturnsProvidedInstance() {
        HttpClient client = mock(HttpClient.class);
        HttpClientResource resource = new HttpClientResource("test", Duration.ofSeconds(5), client);
        assertThat(resource.client()).isSameAs(client);
    }

    @Test
    public void clientAfterCloseThrows() {
        HttpClientResource resource = new HttpClientResource("test", Duration.ofSeconds(5), mock(HttpClient.class));
        resource.close();
        assertThatThrownBy(resource::client)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("test is closed");
    }

    @Test
    public void closeIsIdempotent() {
        HttpClientResource resource = new HttpClientResource("test", Duration.ofSeconds(5), mock(HttpClient.class));
        assertThatNoException().isThrownBy(() -> {
            resource.close();
            resource.close();
        });
    }

    @Test
    public void closeDoesNotTouchProvidedClient() {
        HttpClient client = mock(HttpClient.class);
        HttpClientResource resource = new HttpClientResource("test", Duration.ofSeconds(5), client);
        resource.close();
        verify(client, never()).close();
    }

    @Test
    public void positiveDurationRejectsZero() {
        assertThatThrownBy(() -> HttpClientResource.positiveDuration("connectTimeout", Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("connectTimeout must be positive");
    }

    @Test
    public void positiveDurationRejectsNegative() {
        assertThatThrownBy(() -> HttpClientResource.positiveDuration("connectTimeout", Duration.ofSeconds(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("connectTimeout must be positive");
    }

    @Test
    public void positiveDurationReturnsSameValue() {
        Duration timeout = Duration.ofSeconds(5);
        assertThat(HttpClientResource.positiveDuration("connectTimeout", timeout)).isEqualTo(timeout);
    }
}
