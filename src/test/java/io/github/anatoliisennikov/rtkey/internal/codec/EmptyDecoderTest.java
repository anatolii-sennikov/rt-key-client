package io.github.anatoliisennikov.rtkey.internal.codec;

import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.exception.RtKeyContractException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmptyDecoderTest {

    @Test
    public void acceptsEmptyBodyOnSuccess() {
        assertThatCode(() -> EmptyDecoder.decode(new Response(200, ""))).doesNotThrowAnyException();
    }

    @Test
    public void acceptsBlankBodyOnSuccess() {
        assertThatCode(() -> EmptyDecoder.decode(new Response(204, "   "))).doesNotThrowAnyException();
    }

    @Test
    public void throwsContractOnNonEmptyBody() {
        assertThatThrownBy(() -> EmptyDecoder.decode(new Response(200, "{\"data\":{}}")))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("empty response carries a body (HTTP 200)");
    }

    @Test
    public void throwsApiExceptionOnErrorStatus() {
        Response err = new Response(422,
                "{\"error\":{\"message\":\"boom\",\"reasons\":[{\"type\":\"VALIDATION\"}]}}");

        assertThatThrownBy(() -> EmptyDecoder.decode(err))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessageContaining("boom");
    }

    @Test
    public void throwsApiExceptionOnErrorStatusWithBlankBody() {
        assertThatThrownBy(() -> EmptyDecoder.decode(new Response(503, "")))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessageContaining("API error (HTTP 503)");
    }
}
