package io.github.anatoliisennikov.rtkey.internal.transport;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpTransportTest {

    private static final URI FILE = URI.create("https://household.key.rt.ru/file");

    @Mock
    private HttpClient httpClient;

    private HttpTransport transport;

    @BeforeEach
    public void setUp() {
        transport = new HttpTransport(
                new HttpClientResource("test", Duration.ofSeconds(5), httpClient),
                Hosts.production(),
                Duration.ofSeconds(20));
    }

    @Test
    public void getSendsGetWithBearer() throws Exception {
        stubSend(response(200, "{}"));

        transport.get(Host.HOUSEHOLD, "/v1/x", () -> "tok");

        HttpRequest request = captured();
        assertThat(request.method()).isEqualTo("GET");
        assertThat(request.uri()).isEqualTo(URI.create("https://household.key.rt.ru/v1/x"));
        assertThat(request.headers().firstValue("Authorization")).contains("Bearer tok");
        assertThat(request.headers().firstValue("Accept")).contains("application/json");
        assertThat(request.headers().firstValue("Content-Type")).isEmpty();
    }

    @Test
    public void getQueryResolvesUri() throws Exception {
        stubSend(response(200, "{}"));

        transport.get(Host.HOUSEHOLD, "/v1/x", Query.of("a", "1"), () -> "tok");

        assertThat(captured().uri()).isEqualTo(URI.create("https://household.key.rt.ru/v1/x?a=1"));
    }

    @Test
    public void responseRetainsContentTypeAndRequestContextForDiagnostics() throws Exception {
        HttpResponse<String> upstream = response(200, "<html>error</html>");
        when(upstream.headers()).thenReturn(HttpHeaders.of(
                Map.of("Content-Type", List.of("text/html; charset=UTF-8")), (name, value) -> true));
        stubSend(upstream);

        Response result = transport.get(Host.KEYAPIS,
                "/vc/api/v1/camera_video_data/list", () -> "tok");

        assertThat(result.contentType()).isEqualTo("text/html; charset=UTF-8");
        assertThat(result.request())
                .isEqualTo("GET keyapis.key.rt.ru /vc/api/v1/camera_video_data/list");
    }

    @Test
    public void postWithBodySetsContentType() throws Exception {
        stubSend(response(200, "{}"));

        transport.post(Host.HOUSEHOLD, "/v1/x", Body.json("{\"a\":1}"), () -> "tok");

        HttpRequest request = captured();
        assertThat(request.method()).isEqualTo("POST");
        assertThat(request.headers().firstValue("Content-Type")).contains("application/json");
        assertThat(request.headers().firstValue("Accept")).contains("application/json");
        assertThat(request.headers().firstValue("Authorization")).contains("Bearer tok");
    }

    @Test
    public void postIdentityBodySkipsAccept() throws Exception {
        stubSend(response(200, "{}"));

        transport.post(Host.HOUSEHOLD, "/v1/x", Body.identity("raw"), () -> "tok");

        HttpRequest request = captured();
        assertThat(request.headers().firstValue("Content-Type")).contains("text/plain;charset=UTF-8");
        assertThat(request.headers().firstValue("Accept")).isEmpty();
    }

    @Test
    public void postDeviceIdSetsHeaderNoAuth() throws Exception {
        stubSend(response(200, "{}"));
        UUID deviceId = UUID.fromString("11111111-2222-3333-4444-555555555555");

        transport.post(Host.HOUSEHOLD, "/v1/x", Body.json("{}"), deviceId);

        HttpRequest request = captured();
        assertThat(request.headers().firstValue("X-Device-Id")).contains(deviceId.toString());
        assertThat(request.headers().firstValue("Authorization")).isEmpty();
    }

    @Test
    public void deleteSendsDelete() throws Exception {
        stubSend(response(200, "{}"));

        transport.delete(Host.HOUSEHOLD, "/v1/x", () -> "tok");

        HttpRequest request = captured();
        assertThat(request.method()).isEqualTo("DELETE");
        assertThat(request.headers().firstValue("Content-Type")).isEmpty();
        assertThat(request.headers().firstValue("Accept")).contains("application/json");
    }

    @Test
    public void patchSendsPatch() throws Exception {
        stubSend(response(200, "{}"));

        transport.patch(Host.HOUSEHOLD, "/v1/x", Body.json("{}"), () -> "tok");

        HttpRequest request = captured();
        assertThat(request.method()).isEqualTo("PATCH");
        assertThat(request.headers().firstValue("Content-Type")).contains("application/json");
    }

    @Test
    public void postWithoutBodySkipsContentType() throws Exception {
        stubSend(response(200, "{}"));

        transport.post(Host.HOUSEHOLD, "/v1/x", () -> "tok");

        HttpRequest request = captured();
        assertThat(request.method()).isEqualTo("POST");
        assertThat(request.headers().firstValue("Content-Type")).isEmpty();
        assertThat(request.headers().firstValue("Accept")).contains("application/json");
    }

    @Test
    public void connectTimeoutMapsToNetworkException() throws Exception {
        stubSendThrows(new HttpConnectTimeoutException("connect"));

        assertThatThrownBy(() -> transport.get(Host.HOUSEHOLD, "/v1/x", () -> "tok"))
                .isInstanceOf(RtKeyNetworkException.class)
                .hasMessageContaining("HTTP connect timed out")
                .hasMessageContaining("GET household.key.rt.ru /v1/x")
                .hasCauseInstanceOf(HttpConnectTimeoutException.class);
    }

    @Test
    public void requestTimeoutMapsToNetworkException() throws Exception {
        stubSendThrows(new HttpTimeoutException("read"));

        assertThatThrownBy(() -> transport.get(Host.HOUSEHOLD, "/v1/x", () -> "tok"))
                .isInstanceOf(RtKeyNetworkException.class)
                .hasMessageContaining("HTTP request timed out after 20000 ms")
                .hasCauseInstanceOf(HttpTimeoutException.class);
    }

    @Test
    public void ioExceptionMapsToNetworkExceptionWithRootCause() throws Exception {
        stubSendThrows(new IOException("wrapper", new IOException("root detail")));

        assertThatThrownBy(() -> transport.get(Host.HOUSEHOLD, "/v1/x", () -> "tok"))
                .isInstanceOf(RtKeyNetworkException.class)
                .hasMessageContaining("HTTP call failed")
                .hasMessageContaining("root detail")
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    public void ioExceptionWithoutMessageFallsBackToCauseName() throws Exception {
        stubSendThrows(new IOException("wrapper", new RuntimeException()));

        assertThatThrownBy(() -> transport.get(Host.HOUSEHOLD, "/v1/x", () -> "tok"))
                .isInstanceOf(RtKeyNetworkException.class)
                .hasMessageContaining("RuntimeException");
    }

    @Test
    public void interruptedMapsAndRestoresFlag() throws Exception {
        stubSendThrows(new InterruptedException());
        try {
            assertThatThrownBy(() -> transport.get(Host.HOUSEHOLD, "/v1/x", () -> "tok"))
                    .isInstanceOf(RtKeyNetworkException.class)
                    .hasMessageContaining("HTTP call interrupted")
                    .hasCauseInstanceOf(InterruptedException.class);
            assertThat(Thread.currentThread().isInterrupted()).isTrue();
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    public void emptyTokenThrowsIllegalStateNotWrapped() {
        assertThatThrownBy(() -> transport.get(Host.HOUSEHOLD, "/v1/x", () -> " "))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("tokenSupplier returned null/blank token");
        verifyNoInteractions(httpClient);
    }

    @Test
    public void getBytesReturnsBody() throws Exception {
        stubSend(response(200, new byte[]{1, 2, 3}));

        assertThat(transport.getBytes(FILE, Headers.empty())).containsExactly(1, 2, 3);
    }

    @Test
    public void getBytesNon200TruncatesDetail() throws Exception {
        stubSend(response(500, "a".repeat(300).getBytes(StandardCharsets.UTF_8)));

        assertThatThrownBy(() -> transport.getBytes(FILE, Headers.empty()))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("a".repeat(256));
    }

    @Test
    public void getBytesNon200EmptyBodyFallsBack() throws Exception {
        stubSend(response(500, new byte[0]));

        assertThatThrownBy(() -> transport.getBytes(FILE, Headers.empty()))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("API error (HTTP 500)");
    }

    @Test
    public void openStreamReturnsStream() throws Exception {
        InputStream in = mock(InputStream.class);
        stubSend(response(200, in));

        assertThat(transport.openStream(FILE, Headers.empty())).isSameAs(in);
    }

    @Test
    public void openStreamNon200ClosesStreamAndThrows() throws Exception {
        InputStream in = mock(InputStream.class);
        stubSend(response(404, in));

        assertThatThrownBy(() -> transport.openStream(FILE, Headers.empty()))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("API error (HTTP 404)");
        verify(in).close();
    }

    @Test
    public void openStreamCloseFailureIgnored() throws Exception {
        InputStream in = mock(InputStream.class);
        doThrow(new IOException("close failed")).when(in).close();
        stubSend(response(404, in));

        assertThatThrownBy(() -> transport.openStream(FILE, Headers.empty()))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessage("API error (HTTP 404)");
    }

    @Test
    public void constructorRejectsNonPositiveTimeout() {
        assertThatThrownBy(() -> new HttpTransport(
                new HttpClientResource("test", Duration.ofSeconds(5), httpClient),
                Hosts.production(),
                Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("requestTimeout must be positive");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void stubSend(HttpResponse<?> response) throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void stubSendThrows(Throwable failure) throws IOException, InterruptedException {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenThrow(failure);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private HttpRequest captured() throws IOException, InterruptedException {
        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).send(captor.capture(), any(HttpResponse.BodyHandler.class));
        return captor.getValue();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> HttpResponse<T> response(int status, T body) {
        HttpResponse<T> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(status);
        when(response.body()).thenReturn(body);

        lenient().when(response.headers()).thenReturn(HttpHeaders.of(Map.of(), (name, value) -> true));
        return response;
    }
}
