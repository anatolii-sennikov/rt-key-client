package io.github.anatoliisennikov.rtkey.internal.transport;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public final class HttpTransport {
    static final String JSON = "application/json";
    static final String IDENTITY = "text/plain;charset=UTF-8";
    private static final String ACCEPT = "Accept";
    private static final String CONTENT_TYPE = "Content-Type";

    public static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(20);

    private final HttpClientResource resource;
    private final Hosts hosts;
    private final Duration requestTimeout;

    public HttpTransport(HttpClientResource resource, Hosts hosts, Duration requestTimeout) {
        this.resource = Objects.requireNonNull(resource, "resource");
        this.hosts = Objects.requireNonNull(hosts, "hosts");
        this.requestTimeout = HttpClientResource.positiveDuration("requestTimeout", requestTimeout);
    }

    public Response get(Host host, String path, Supplier<String> tokenSupplier) {
        return get(host, path, Query.empty(), tokenSupplier);
    }

    public Response get(Host host, String path, Query query, Supplier<String> tokenSupplier) {
        return exchange("GET", host, path, query, null, Headers.bearer(tokenSupplier));
    }

    public Response post(Host host, String path, Body body, UUID deviceId) {
        return exchange("POST", host, path, Query.empty(), body, Headers.deviceId(deviceId));
    }

    public Response post(Host host, String path, Supplier<String> tokenSupplier) {
        return exchange("POST", host, path, Query.empty(), null, Headers.bearer(tokenSupplier));
    }

    public Response post(Host host, String path, Body body, Supplier<String> tokenSupplier) {
        return exchange("POST", host, path, Query.empty(), body, Headers.bearer(tokenSupplier));
    }

    public Response patch(Host host, String path, Body body, Supplier<String> tokenSupplier) {
        return exchange("PATCH", host, path, Query.empty(), body, Headers.bearer(tokenSupplier));
    }

    public Response delete(Host host, String path, Supplier<String> tokenSupplier) {
        return exchange("DELETE", host, path, Query.empty(), null, Headers.bearer(tokenSupplier));
    }

    public byte[] getBytes(URI uri, Headers headers) {
        Objects.requireNonNull(uri, "uri");
        Objects.requireNonNull(headers, "headers");

        HttpRequest.Builder builder = HttpRequest.newBuilder(uri).timeout(requestTimeout);
        for (Headers.Entry entry : headers.entries()) {
            builder.header(entry.name(), entry.value());
        }

        HttpRequest request = builder.GET().build();

        String context = "GET " + Redacted.redact(uri);
        HttpResponse<byte[]> response = send(resource, request, context, requestTimeout, HttpResponse.BodyHandlers.ofByteArray());

        int status = response.statusCode();

        if (status != 200) {
            byte[] body = response.body();
            String detail = body.length == 0 ? null
                    : new String(body, 0, Math.min(body.length, 256), StandardCharsets.UTF_8);

            throw new RtKeyApiException(status, null, null, detail, List.of());
        }
        return response.body();
    }

    public InputStream openStream(URI uri, Headers headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(uri).timeout(requestTimeout);
        for (Headers.Entry entry : headers.entries()) {
            builder.header(entry.name(), entry.value());
        }

        HttpRequest request = builder.GET().build();

        String context = "GET " + Redacted.redact(uri);
        HttpResponse<InputStream> rawResponse = send(resource, request, context, requestTimeout, HttpResponse.BodyHandlers.ofInputStream());

        if (rawResponse.statusCode() != 200) {
            try {
                rawResponse.body().close();
            } catch (IOException ignored) {
                // The response is already an error.
            }
            throw new RtKeyApiException(rawResponse.statusCode(), null, null, null, List.of());
        }
        return rawResponse.body();

    }

    private Response exchange(String method, Host host, String path, Query query,
                              @Nullable Body body, Headers headers) {
        Objects.requireNonNull(headers, "headers");

        URI uri = hosts.resolve(host, path, query);

        HttpRequest.Builder request = HttpRequest.newBuilder(uri)
                .timeout(requestTimeout)
                .method(method, body == null
                        ? HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(body.payload(), StandardCharsets.UTF_8));

        for (Headers.Entry entry : headers.entries()) {
            request.header(entry.name(), entry.value());
        }

        if (body != null) {
            request.header(CONTENT_TYPE, body.contentType());
        }
        if (body == null || !body.identity()) {
            request.header(ACCEPT, JSON);
        }

        String context = method + " " + uri.getHost() + " " + uri.getPath();
        HttpResponse<String> response = send(resource, request.build(), context, requestTimeout,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        String contentType = response.headers().firstValue(CONTENT_TYPE).orElse("");
        return new Response(response.statusCode(), response.body(), contentType, context);
    }

    private static <T> HttpResponse<T> send(HttpClientResource resource, HttpRequest request,
                                            String context, Duration timeout,
                                            HttpResponse.BodyHandler<T> handler) {
        try {
            return resource.client().send(request, handler);
        } catch (HttpConnectTimeoutException e) {
            throw new RtKeyNetworkException("HTTP connect timed out: " + context, e);
        } catch (HttpTimeoutException e) {
            throw new RtKeyNetworkException("HTTP request timed out after " + timeout.toMillis()
                    + " ms: " + context, e);
        } catch (IOException e) {
            throw new RtKeyNetworkException("HTTP call failed: " + context + ": " + reason(e), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RtKeyNetworkException("HTTP call interrupted: " + context, e);
        }
    }

    private static String reason(IOException failure) {
        Throwable cause = failure;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage() != null ? cause.getMessage() : cause.getClass().getSimpleName();
    }
}
