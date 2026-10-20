package io.github.anatoliisennikov.rtkey.internal.transport;

import org.jspecify.annotations.Nullable;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public final class HttpClientResource  implements AutoCloseable {
    public static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(5);

    private final String owner;
    private final HttpClient httpClient;
    private final boolean ownsHttpClient;
    private final AtomicBoolean closed = new AtomicBoolean();

    public HttpClientResource(String owner, Duration connectTimeout, @Nullable HttpClient httpClient) {
        this.owner = Objects.requireNonNull(owner, "owner");
        if (httpClient != null) {
            this.httpClient = httpClient;
            this.ownsHttpClient = false;
        } else {
            this.httpClient = create(connectTimeout);
            this.ownsHttpClient = true;
        }
    }

    public HttpClient client() {
        if (closed.get()) {
            throw new IllegalStateException(owner + " is closed");
        }
        return httpClient;
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true) && ownsHttpClient) {
            httpClient.close();
        }
    }

    public static Duration positiveDuration(String name, Duration duration) {
        Objects.requireNonNull(duration, name);
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException(name + " must be positive");
        }
        return duration;
    }

    private static HttpClient create(Duration connectTimeout) {
        return HttpClient.newBuilder()
                .connectTimeout(positiveDuration("connectTimeout", connectTimeout))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }
}
