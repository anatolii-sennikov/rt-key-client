package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public abstract class BaseOperationTest {
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String IDENTITY_CONTENT_TYPE = "text/plain;charset=UTF-8";

    protected final Supplier<String> tokenSupplier = () -> "anystring";

    @Mock
    protected HttpTransport transport;

    protected String getResourceAsString(String path) throws IOException {
        try (InputStream in = Objects.requireNonNull(
                getClass().getResourceAsStream(path), "fixture not found: " + path)) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    protected static void assertJsonBody(Body body, String expectedPayload) {
        assertThat(body.contentType()).isEqualTo(JSON_CONTENT_TYPE);
        assertThat(body.payload()).isEqualTo(expectedPayload);
    }

    protected static void assertIdentityBody(Body body, String expectedPayload) {
        assertThat(body.contentType()).isEqualTo(IDENTITY_CONTENT_TYPE);
        assertThat(body.payload()).isEqualTo(expectedPayload);
    }
}
