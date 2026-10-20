package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.internal.identity.LoginOperation;
import io.github.anatoliisennikov.rtkey.internal.identity.SendCodeOperation;
import io.github.anatoliisennikov.rtkey.internal.transport.Hosts;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpClientResource;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.CodeDispatch;
import io.github.anatoliisennikov.rtkey.model.Session;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Authentication client for the RT Key API.
 *
 * <p>Performs the two authentication steps:
 * <ul>
 *   <li>{@link #sendCode(String)} - sends a verification code to a phone number</li>
 *   <li>{@link #login(CodeDispatch, String)} - exchanges the received code for a session</li>
 * </ul>
 *
 * <p>Create an instance with {@link #builder()} and the returned {@link Builder}.
 * This class implements {@link AutoCloseable}; call {@link #close()} when the client
 * is no longer needed.
 */
public final class RtKeyAuth implements AutoCloseable {
    private final HttpClientResource resource;
    private final HttpTransport httpTransport;
    private final UUID deviceId;

    private RtKeyAuth(HttpClientResource resource, HttpTransport httpTransport, UUID deviceId) {
        this.resource = resource;
        this.httpTransport = httpTransport;
        this.deviceId = deviceId;
    }

    /**
     * Sends a verification code to the given phone number.
     *
     * @param phoneNumber the phone number
     * @return the code dispatch to pass to {@link #login(CodeDispatch, String)}
     * @throws NullPointerException if {@code phoneNumber} is {@code null}
     */
    public CodeDispatch sendCode(String phoneNumber) {
        Objects.requireNonNull(phoneNumber, "phoneNumber must not be null");
        return SendCodeOperation.call(httpTransport, deviceId, phoneNumber);
    }

    /**
     * Exchanges a verification code and its dispatch for a session.
     *
     * @param codeDispatch the dispatch returned by {@link #sendCode(String)}; the client device ID
     *                     must be the same as for that call
     * @param code         the verification code received on the phone via SMS
     * @return the session containing the access token and its expiry time
     * @throws NullPointerException if {@code code} or {@code codeDispatch} is {@code null}
     */
    public Session login(CodeDispatch codeDispatch, String code) {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(codeDispatch, "codeDispatch must not be null");
        return LoginOperation.call(httpTransport, deviceId, code, codeDispatch.codeId());
    }

    /**
     * Creates a new builder for {@link RtKeyAuth}.
     *
     * @return a new {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
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
     * Builder for {@link RtKeyAuth}.
     *
     * <p>{@link #deviceId(UUID)} is required. {@link #baseUri(Host, URI)} supports
     * only {@link Host#KEYAPIS}; by default the production host is used.
     *
     * <p>When the SDK creates its own {@link HttpClient} and the connect timeout is not set,
     * the connect timeout is {@code 5s}. The request timeout defaults to {@code 20s} in all cases.
     */
    public static final class Builder {

        private @Nullable Duration connectTimeout;
        private Duration requestTimeout = HttpTransport.DEFAULT_REQUEST_TIMEOUT;
        private @Nullable HttpClient httpClient;
        private @Nullable UUID deviceId;
        private @Nullable URI keyapisBaseUri;

        private Builder() {
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
         * Sets the device ID.
         *
         * <p>The device ID is sent as the {@code X-Device-Id} header. It must be the
         * same for {@link RtKeyAuth#sendCode(String)} and
         * {@link RtKeyAuth#login(CodeDispatch, String)}; the server rejects a login
         * that uses a different device ID. Both calls on the same client use this ID
         * automatically.
         *
         * @param deviceId the device ID, required
         * @return this builder
         * @throws NullPointerException if {@code deviceId} is {@code null}
         */
        public Builder deviceId(UUID deviceId) {
            this.deviceId = Objects.requireNonNull(deviceId, "deviceId");
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
         * Sets a custom base URI for {@link Host#KEYAPIS}.
         *
         * @param host    the host, must be {@link Host#KEYAPIS}
         * @param baseUri the custom base URI
         * @return this builder
         * @throws NullPointerException if {@code host} or {@code baseUri} is {@code null}
         * @throws IllegalArgumentException if {@code host} is not {@link Host#KEYAPIS}
         * or {@code baseUri} is invalid
         */
        public Builder baseUri(Host host, URI baseUri) {
            if (Objects.requireNonNull(host, "host") != Host.KEYAPIS) {
                throw new IllegalArgumentException("RtKeyAuth overrides only KEYAPIS, got " + host);
            }
            this.keyapisBaseUri = Hosts.checkBaseUri(baseUri);
            return this;
        }

        /**
         * Builds the {@link RtKeyAuth} instance.
         *
         * @return a new {@link RtKeyAuth}
         * @throws NullPointerException if {@code deviceId} is not set
         * @throws IllegalStateException if {@code connectTimeout} is combined with {@code httpClient}
         */
        public RtKeyAuth build() {
            if (httpClient != null && connectTimeout != null) {
                throw new IllegalStateException("connectTimeout cannot be combined with httpClient: "
                        + "set the connect timeout on your own HttpClient");
            }

            Objects.requireNonNull(deviceId, "deviceId is required");

            HttpClientResource resource = new HttpClientResource("RtKeyAuth",
                    connectTimeout != null ? connectTimeout : HttpClientResource.DEFAULT_CONNECT_TIMEOUT,
                    httpClient);
            Hosts hosts = keyapisBaseUri != null
                    ? Hosts.withOverrides(Map.of(Host.KEYAPIS, keyapisBaseUri))
                    : Hosts.production();
            HttpTransport transport = new HttpTransport(resource, hosts, requestTimeout);
            return new RtKeyAuth(resource, transport, deviceId);
        }
    }
}
