package io.github.anatoliisennikov.rtkey.internal.codec;

import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.exception.RtKeyContractException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;


public class CamelCaseDecoderTest {
    record CodePayload(String codeId, Integer timeout) {
    }

    record CameraPayload(String uid, String title) {
    }

    record ItemPayload(Integer id, String name) {
    }

    @Test
    public void dataDecodesCamelCaseDataNode() {
        Response ok = new Response(200, "{\"data\":{\"codeId\":\"somethingCode\",\"timeout\":60}}");

        CodePayload payload = CamelCaseDecoder.data(ok, CodePayload.class);

        assertThat(payload.codeId()).isEqualTo("somethingCode");
        assertThat(payload.timeout()).isEqualTo(60);
    }

    @Test
    public void dataIgnoresUnknownProperties() {
        Response ok = new Response(200, "{\"data\":{\"codeId\":\"somethingCode\",\"timeout\":60,\"futureField\":1}}");

        CodePayload payload = CamelCaseDecoder.data(ok, CodePayload.class);

        assertThat(payload.codeId()).isEqualTo("somethingCode");
    }

    @Test
    public void dataDoesNotApplySnakeCase() {
        Response ok = new Response(200, "{\"data\":{\"code_id\":\"c-1\",\"timeout\":60}}");

        CodePayload payload = CamelCaseDecoder.data(ok, CodePayload.class);

        assertThat(payload.codeId()).isNull();
        assertThat(payload.timeout()).isEqualTo(60);
    }

    @Test
    public void dataDecodesArrayPayload() {
        Response ok = new Response(200,
                "{\"data\":[{\"uid\":\"a\",\"title\":\"cam1\"},{\"uid\":\"b\",\"title\":\"cam2\"}]}");
        TypeReference<List<CameraPayload>> list = new TypeReference<>() {
        };

        List<CameraPayload> payload = CamelCaseDecoder.data(ok, list);

        assertThat(payload).hasSize(2);
        assertThat(payload.get(0).uid()).isEqualTo("a");
        assertThat(payload.get(1).title()).isEqualTo("cam2");
    }

    @Test
    public void dataThrowsContractWhenDataMissingOrNull() {
        TypeReference<CodePayload> type = new TypeReference<>() {
        };

        assertThatThrownBy(() -> CamelCaseDecoder.data(new Response(200, "{}"), type))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data is missing or null");
        assertThatThrownBy(() -> CamelCaseDecoder.data(new Response(200, "{\"data\":null}"), type))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data is missing or null");
    }

    @Test
    public void dataThrowsContractOnEmptyBody() {
        TypeReference<CodePayload> type = new TypeReference<>() {
        };

        assertThatThrownBy(() -> CamelCaseDecoder.data(new Response(200, ""), type))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("has no body");
    }

    @Test
    public void dataThrowsContractOnNonJsonOrNonObjectRoot() {
        TypeReference<CodePayload> type = new TypeReference<>() {
        };

        assertThatThrownBy(() -> CamelCaseDecoder.data(new Response(200, "not json"), type))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not JSON");
        assertThatThrownBy(() -> CamelCaseDecoder.data(new Response(200, "[1,2]"), type))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not a JSON object");
    }

    @Test
    public void nonJsonDiagnosticsIncludeResponseMetadataAndBoundedBodyPreview() {
        String html = "<!doctype html>\n<html><title>Gateway error</title>" + "x".repeat(2048);
        Response response = new Response(200, html, "text/html; charset=UTF-8",
                "GET keyapis.key.rt.ru /vc/api/v1/camera_video_data/list");

        Throwable thrown = Objects.requireNonNull(
                catchThrowable(() -> CamelCaseDecoder.data(response, CodePayload.class)));
        Throwable diagnostic = Objects.requireNonNull(thrown.getCause());

        assertThat(thrown)
                .isInstanceOf(RtKeyContractException.class)
                .hasMessage("response is not JSON");
        assertThat(diagnostic)
                .hasMessageContaining("GET keyapis.key.rt.ru /vc/api/v1/camera_video_data/list")
                .hasMessageContaining("HTTP status=200")
                .hasMessageContaining("Content-Type=text/html; charset=UTF-8")
                .hasMessageContaining("<!doctype html> <html><title>Gateway error</title>")
                .hasMessageContaining("[truncated]");
        assertThat(diagnostic.getMessage()).doesNotContain("x".repeat(1025));
    }

    @Test
    public void dataThrowsContractOnPayloadMismatch() {
        Response ok = new Response(200, "{\"data\":{\"codeId\":\"x\"}}");

        assertThatThrownBy(() -> CamelCaseDecoder.data(ok, Integer.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("does not match");
    }

    @Test
    public void dataThrowsApiExceptionOnErrorStatus() {
        Response err = new Response(401, "{\"code\":401,\"message\":\"bad token\"}");

        assertThatThrownBy(() -> CamelCaseDecoder.data(err, CodePayload.class))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessageContaining("bad token");
    }

    @Test
    public void resultItemsDecodesItemsByClass() {
        Response ok = new Response(200,
                "[{\"result\":{\"data\":{\"id\":1,\"name\":\"a\"}}},{\"result\":{\"data\":{\"id\":2,\"name\":\"b\"}}}]");

        List<ItemPayload> items = CamelCaseDecoder.resultItems(ok, ItemPayload.class);

        assertThat(items).hasSize(2);
        assertThat(items.get(0).id()).isEqualTo(1);
        assertThat(items.get(1).name()).isEqualTo("b");
    }

    @Test
    public void resultItemsDecodesScalarItems() {
        Response ok = new Response(200, "[{\"result\":{\"data\":\"C_9944\"}},{\"result\":{\"data\":\"O_1\"}}]");
        TypeReference<String> itemType = new TypeReference<>() {
        };

        List<String> items = CamelCaseDecoder.resultItems(ok, itemType);

        assertThat(items).containsExactly("C_9944", "O_1");
    }

    @Test
    public void resultItemsIgnoresUnknownProperties() {
        Response ok = new Response(200, "[{\"result\":{\"data\":{\"id\":1,\"name\":\"a\",\"futureField\":1}}}]");

        List<ItemPayload> items = CamelCaseDecoder.resultItems(ok, ItemPayload.class);

        assertThat(items).hasSize(1);
        assertThat(items.get(0).id()).isEqualTo(1);
    }

    @Test
    public void resultItemsDecodesEmptyArray() {
        Response ok = new Response(200, "[]");

        assertThat(CamelCaseDecoder.resultItems(ok, ItemPayload.class)).isEmpty();
    }

    @Test
    public void resultItemsThrowsWhenItemHasNoResultData() {
        assertThatThrownBy(() -> CamelCaseDecoder.resultItems(
                new Response(200, "[{\"result\":{}}]"), ItemPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("result item 0: result.data is missing or null");
        assertThatThrownBy(() -> CamelCaseDecoder.resultItems(
                new Response(200, "[{\"result\":{\"data\":null}}]"), ItemPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("result item 0: result.data is missing or null");
        assertThatThrownBy(() -> CamelCaseDecoder.resultItems(
                new Response(200, "[{}]"), ItemPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("result item 0: result.data is missing or null");
    }

    @Test
    public void resultItemsThrowsWithIndexOnItemMismatch() {
        Response ok = new Response(200,
                "[{\"result\":{\"data\":{\"id\":1,\"name\":\"a\"}}},{\"result\":{\"data\":\"not-an-object\"}}]");

        assertThatThrownBy(() -> CamelCaseDecoder.resultItems(ok, ItemPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("result item 1")
                .hasMessageContaining("does not match");
    }

    @Test
    public void resultItemsThrowsOnEmptyBody() {
        assertThatThrownBy(() -> CamelCaseDecoder.resultItems(new Response(200, ""), ItemPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("has no body");
    }

    @Test
    public void resultItemsThrowsOnNonJsonOrNonArrayRoot() {
        assertThatThrownBy(() -> CamelCaseDecoder.resultItems(new Response(200, "not json"), ItemPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not JSON");
        assertThatThrownBy(() -> CamelCaseDecoder.resultItems(new Response(200, "{\"result\":[]}"), ItemPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not a JSON array");
    }

    @Test
    public void resultItemsThrowsApiExceptionOnErrorStatus() {
        Response err = new Response(422,
                "{\"error\":{\"message\":\"boom\",\"reasons\":[{\"type\":\"VALIDATION\"}]}}");

        assertThatThrownBy(() -> CamelCaseDecoder.resultItems(err, ItemPayload.class))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessageContaining("boom");
    }
}
