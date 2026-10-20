package io.github.anatoliisennikov.rtkey.internal.codec;

import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerErrorsTest {

    @Test
    public void parsesReasonsForm() {
        Response response = new Response(422,
                "{\"error\":{\"message\":\"fail\",\"reasons\":[{\"type\":\"VALIDATION\",\"message\":\"inner\","
                        + "\"details\":[{\"property\":\"flat_id\",\"description\":\"empty\"}]}]}}");

        RtKeyApiException error = ServerErrors.of(response);

        assertThat(error.getStatus()).isEqualTo(422);
        assertThat(error.getServerCode()).isEqualTo("VALIDATION");
        assertThat(error.getMessage()).isEqualTo("inner: flat_id: empty");
        assertThat(error.getDescription()).containsExactly("flat_id: empty");
    }

    @Test
    public void parsesCodeForm() {
        Response response = new Response(403,
                "{\"error\":{\"code\":10,\"title\":\"Denied\",\"description\":\"no access\"}}");

        RtKeyApiException error = ServerErrors.of(response);

        assertThat(error.getStatus()).isEqualTo(403);
        assertThat(error.getServerCode()).isEqualTo("10");
        assertThat(error.getMessage()).isEqualTo("Denied: no access");
        assertThat(error.getDescription()).containsExactly("no access");
    }

    @Test
    public void parsesValidationForm() {
        Response response = new Response(422,
                "{\"message\":\"Validation Failure\",\"metas\":[{\"field\":\"flat_id\",\"reason\":\"empty\"}]}");

        RtKeyApiException error = ServerErrors.of(response);

        assertThat(error.getStatus()).isEqualTo(422);
        assertThat(error.getServerCode()).isNull();
        assertThat(error.getMessage()).isEqualTo("Validation Failure: flat_id: empty");
        assertThat(error.getDescription()).containsExactly("flat_id: empty");
    }

    @Test
    public void parsesFlatIdentityForm() {
        Response response = new Response(401, "{\"code\":401,\"message\":\"bad token\"}");

        RtKeyApiException error = ServerErrors.of(response);

        assertThat(error.getStatus()).isEqualTo(401);
        assertThat(error.getServerCode()).isEqualTo("401");
        assertThat(error.getMessage()).isEqualTo("bad token");
        assertThat(error.getDescription()).isEmpty();
    }

    @Test
    public void fallsBackToStatusOnNonJsonBody() {
        RtKeyApiException error = ServerErrors.of(new Response(500, "<html>oops"));

        assertThat(error.getStatus()).isEqualTo(500);
        assertThat(error.getServerCode()).isNull();
        assertThat(error.getMessage()).isEqualTo("API error (HTTP 500)");
        assertThat(error.getDescription()).isEmpty();
    }

    @Test
    public void fallsBackToStatusOnBlankBody() {
        RtKeyApiException error = ServerErrors.of(new Response(503, ""));

        assertThat(error.getStatus()).isEqualTo(503);
        assertThat(error.getMessage()).isEqualTo("API error (HTTP 503)");
    }

    @Test
    public void propagatesRootRequestId() {
        Response response = new Response(422,
                "{\"request_id\":\"req-1\",\"error\":{\"message\":\"boom\",\"reasons\":[{\"type\":\"VALIDATION\"}]}}");

        assertThat(ServerErrors.of(response).getRequestId()).isEqualTo("req-1");
    }

    @Test
    public void propagatesNestedRequestId() {
        Response response = new Response(422,
                "{\"error\":{\"request_id\":\"req-2\",\"message\":\"boom\",\"reasons\":[{\"type\":\"VALIDATION\"}]}}");

        assertThat(ServerErrors.of(response).getRequestId()).isEqualTo("req-2");
    }

    @Test
    public void parsesWrappedKeyapisForm() {
        Response response = new Response(401,
                "{\"error\":{\"code\":16,\"message\":\"access token is invalid\",\"details\":[]}}");

        RtKeyApiException error = ServerErrors.of(response);

        assertThat(error.getStatus()).isEqualTo(401);
        assertThat(error.getServerCode()).isEqualTo("16");
        assertThat(error.getMessage()).isEqualTo("access token is invalid");
        assertThat(error.getDescription()).isEmpty();
    }

    @Test
    public void parsesMessageOnlyForm() {
        Response response = new Response(415, "{\"message\":\"Unsupported Media Type, reason: EOF\"}");

        RtKeyApiException error = ServerErrors.of(response);

        assertThat(error.getStatus()).isEqualTo(415);
        assertThat(error.getServerCode()).isNull();
        assertThat(error.getMessage()).isEqualTo("Unsupported Media Type, reason: EOF");
        assertThat(error.getDescription()).isEmpty();
    }

    @Test
    public void firstReasonWinsWhenSeveral() {
        Response response = new Response(422,
                "{\"error\":{\"message\":\"fallback\",\"reasons\":["
                        + "{\"type\":\"FIRST\",\"message\":\"first message\",\"details\":[{\"description\":\"d1\"}]},"
                        + "{\"type\":\"SECOND\",\"message\":\"second message\",\"details\":[{\"description\":\"d2\"}]}]}}");

        RtKeyApiException error = ServerErrors.of(response);

        assertThat(error.getServerCode()).isEqualTo("FIRST");
        assertThat(error.getMessage()).isEqualTo("first message: d1; d2");
        assertThat(error.getDescription()).containsExactly("d1", "d2");
    }
}
