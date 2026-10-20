package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.internal.account.AccountServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.camera.CameraMediaServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.camera.CameraServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.code.CodeServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.device.DeviceServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.events.EventServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.keys.KeysServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.subscription.SubscriptionServiceImpl;
import io.github.anatoliisennikov.rtkey.internal.transport.Hosts;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpClientResource;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Main client for the RT Key API.
 *
 * <p>Exposes domain services through dedicated accessors:
 * <ul>
 *   <li>{@link #account()} - account operations</li>
 *   <li>{@link #devices()} - device operations</li>
 *   <li>{@link #codes()} - door code operations</li>
 *   <li>{@link #keys()} - RFID key operations</li>
 *   <li>{@link #cameras()} - camera operations</li>
 *   <li>{@link #cameraMedia()} - camera media operations</li>
 *   <li>{@link #events()} - event operations</li>
 *   <li>{@link #subscriptions()} - subscription operations</li>
 * </ul>
 *
 * <p>Create an instance with {@link #builder(Supplier)} using a token supplier, or
 * {@link #builder(String)} using a static token string. This class implements
 * {@link AutoCloseable}; call {@link #close()} when the client is no longer needed.
 */
public final class RtKeyClient implements AutoCloseable {

    private final HttpClientResource resource;
    private final AccountService accountService;
    private final DeviceService deviceService;
    private final CodeService codeService;
    private final KeysService keysService;
    private final CameraService cameraService;
    private final EventService eventService;
    private final SubscriptionService subscriptionService;
    private final CameraMediaService cameraMediaService;

    private RtKeyClient(HttpClientResource resource, HttpTransport httpTransport,
                        Supplier<String> tokenSupplier) {
        this.resource = resource;

        accountService = new AccountServiceImpl(httpTransport, tokenSupplier);
        deviceService = new DeviceServiceImpl(httpTransport, tokenSupplier);
        codeService = new CodeServiceImpl(httpTransport, tokenSupplier);
        keysService = new KeysServiceImpl(httpTransport, tokenSupplier);
        cameraService = new CameraServiceImpl(httpTransport, tokenSupplier);
        cameraMediaService = new CameraMediaServiceImpl(httpTransport);
        eventService = new EventServiceImpl(httpTransport, tokenSupplier);
        subscriptionService = new SubscriptionServiceImpl(httpTransport, tokenSupplier);
    }

    /**
     * Returns the account service.
     *
     * @return the account service
     */
    public AccountService account() {
        return accountService;
    }

    /**
     * Returns the device service.
     *
     * @return the device service
     */
    public DeviceService devices() {
        return deviceService;
    }

    /**
     * Returns the door code service.
     *
     * @return the door code service
     */
    public CodeService codes() {
        return codeService;
    }

    /**
     * Returns the RFID key service.
     *
     * @return the RFID key service
     */
    public KeysService keys() {
        return keysService;
    }

    /**
     * Returns the camera service.
     *
     * @return the camera service
     */
    public CameraService cameras() {
        return cameraService;
    }

    /**
     * Returns the camera media service.
     *
     * @return the camera media service
     */
    public CameraMediaService cameraMedia() {
        return cameraMediaService;
    }

    /**
     * Returns the event service.
     *
     * @return the event service
     */
    public EventService events() {
        return eventService;
    }

    /**
     * Returns the subscription service.
     *
     * @return the subscription service
     */
    public SubscriptionService subscriptions() {
        return subscriptionService;
    }

    /**
     * Creates a new builder using a token supplier.
     *
     * <p>The supplier is invoked for every authenticated request and must return a
     * non-null, non-blank token; otherwise the request fails with an
     * {@link IllegalStateException}.
     *
     * @param tokenSupplier the supplier of the bearer token
     * @return a new {@link Builder}
     */
    public static Builder builder(Supplier<String> tokenSupplier) {
        return new Builder(tokenSupplier);
    }

    /**
     * Creates a new builder using a static bearer token.
     *
     * @param token the bearer token, required
     * @return a new {@link Builder}
     * @throws NullPointerException if {@code token} is {@code null}
     */
    public static Builder builder(String token) {
        Objects.requireNonNull(token, "token");
        return builder(() -> token);
    }

    /**
     * Closes this client.
     *
     * <p>This method is idempotent: calling it more than once has no additional effect.
     * If the client was created with a user-supplied {@link HttpClient}, that client is
     * not closed by this method; an internally created client is closed.
     *
     * <p>After the client is closed, any operation that sends a request throws an
     * {@link IllegalStateException}.
     */
    @Override
    public void close() {
        resource.close();
    }

    /**
     * Builder for {@link RtKeyClient}.
     *
     * <p>The bearer token supplier is provided to {@link RtKeyClient#builder(Supplier)}.
     * Base URIs may be overridden per {@link Host} with {@link #baseUri(Host, URI)};
     * by default production hosts are used.
     *
     * <p>When the SDK creates its own {@link HttpClient} and the connect timeout is not set,
     * the connect timeout is {@code 5s}. The request timeout defaults to {@code 20s} in all cases.
     */
    public static final class Builder {
        private @Nullable Duration connectTimeout;
        private Duration requestTimeout = HttpTransport.DEFAULT_REQUEST_TIMEOUT;
        private @Nullable HttpClient httpClient;
        private final Supplier<String> tokenSupplier;
        private final EnumMap<Host, URI> baseUris = new EnumMap<>(Host.class);

        private Builder(Supplier<String> tokenSupplier) {
            this.tokenSupplier = tokenSupplier;
        }

        /**
         * Sets the connect timeout.
         *
         * @param connectTimeout the connect timeout, must be positive
         * @return this builder
         * @throws NullPointerException if {@code connectTimeout} is {@code null}
         * @throws IllegalArgumentException if {@code connectTimeout} is not positive
         */
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = HttpClientResource.positiveDuration("connectTimeout", connectTimeout);
            return this;
        }

        /**
         * Sets the request timeout.
         *
         * @param requestTimeout the request timeout, must be positive
         * @return this builder
         * @throws NullPointerException if {@code requestTimeout} is {@code null}
         * @throws IllegalArgumentException if {@code requestTimeout} is not positive
         */
        public Builder requestTimeout(Duration requestTimeout) {
            this.requestTimeout = HttpClientResource.positiveDuration("requestTimeout", requestTimeout);
            return this;
        }

        /**
         * Sets a custom {@link HttpClient}.
         *
         * <p>The SDK does not close the supplied client. When a custom client is set, the connect
         * timeout cannot also be set; configure it on the client instead.
         *
         * @param httpClient the HTTP client, required
         * @return this builder
         * @throws NullPointerException if {@code httpClient} is {@code null}
         */
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
            return this;
        }

        /**
         * Sets a custom base URI for a host.
         *
         * @param host    the host, required
         * @param baseUri the custom base URI
         * @return this builder
         * @throws NullPointerException if {@code host} or {@code baseUri} is {@code null}
         * @throws IllegalArgumentException if {@code baseUri} is invalid
         */
        public Builder baseUri(Host host, URI baseUri) {
            baseUris.put(Objects.requireNonNull(host, "host"), Hosts.checkBaseUri(baseUri));
            return this;
        }

        /**
         * Builds the {@link RtKeyClient} instance.
         *
         * @return a new {@link RtKeyClient}
         * @throws IllegalStateException if {@code connectTimeout} is combined with {@code httpClient}
         */
        public RtKeyClient build() {
            if (httpClient != null && connectTimeout != null) {
                throw new IllegalStateException("connectTimeout cannot be combined with httpClient: "
                        + "set the connect timeout on your own HttpClient");
            }

            HttpClientResource resource = new HttpClientResource("RtKeyClient",
                    connectTimeout != null ? connectTimeout : HttpClientResource.DEFAULT_CONNECT_TIMEOUT,
                    httpClient);
            Hosts hosts = baseUris.isEmpty() ? Hosts.production() : Hosts.withOverrides(baseUris);
            HttpTransport transport = new HttpTransport(resource, hosts, requestTimeout);

            return new RtKeyClient(resource, transport, tokenSupplier);
        }
    }
}
