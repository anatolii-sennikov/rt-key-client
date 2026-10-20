package io.github.anatoliisennikov.rtkey.internal.codec;

import io.github.anatoliisennikov.rtkey.exception.RtKeyApiException;
import io.github.anatoliisennikov.rtkey.exception.RtKeyContractException;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SnakeCaseDecoderTest {
    record KeyPayload(Integer id, String name, Integer flatId) {
    }

    record CompanyPayload(Integer id, String companyAddress, String description, String logoUrl, String name) {
    }

    record DevicePayload(Integer id, String name) {
    }

    record EventPayload(Integer deviceId, String eventName) {
    }

    @Test
    public void dataDecodesObjectPayload() {
        Response ok = new Response(200, "{\"data\":{\"id\":42,\"title\":\"cam\"}}");

        Map<String, Object> data = SnakeCaseDecoder.data(ok, new TypeReference<>() {
        });

        assertThat(data).containsEntry("id", 42).containsEntry("title", "cam");
    }

    @Test
    public void dataDecodesScalarPayload() {
        Response ok = new Response(200, "{\"data\":280}");

        Integer count = SnakeCaseDecoder.data(ok, Integer.class);

        assertThat(count).isEqualTo(280);
    }

    @Test
    public void dataThrowsContractWhenDataMissingOrNull() {
        TypeReference<Map<String, Object>> map = new TypeReference<>() {
        };

        assertThatThrownBy(() -> SnakeCaseDecoder.data(new Response(200, "{}"), map))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data is missing or null");
        assertThatThrownBy(() -> SnakeCaseDecoder.data(new Response(200, "{\"data\":null}"), map))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data is missing or null");
    }

    @Test
    public void dataThrowsContractOnEmptyBody() {
        TypeReference<Map<String, Object>> map = new TypeReference<>() {
        };

        assertThatThrownBy(() -> SnakeCaseDecoder.data(new Response(200, ""), map))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("has no body");
    }

    @Test
    public void dataThrowsContractOnNonJsonOrNonObjectRoot() {
        TypeReference<Map<String, Object>> map = new TypeReference<>() {
        };

        assertThatThrownBy(() -> SnakeCaseDecoder.data(new Response(200, "not json"), map))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not JSON");
        assertThatThrownBy(() -> SnakeCaseDecoder.data(new Response(200, "[1,2]"), map))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not a JSON object");
    }

    @Test
    public void dataThrowsContractOnPayloadMismatch() {
        assertThatThrownBy(() -> SnakeCaseDecoder.data(new Response(200, "{\"data\":{\"id\":42}}"),
                new TypeReference<Integer>() {
                }))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("does not match");
    }

    @Test
    public void dataThrowsApiExceptionOnErrorStatus() {
        Response err = new Response(422,
                "{\"error\":{\"message\":\"boom\",\"reasons\":[{\"type\":\"VALIDATION\"}]}}");

        assertThatThrownBy(() -> SnakeCaseDecoder.data(err, new TypeReference<Map<String, Object>>() {
        }))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessageContaining("boom");
    }

    @Test
    public void dataNodeDecodesCompanyNode() {
        Response ok = new Response(200,
                "{\"data\":{\"company\":{\"id\":9944,\"company_address\":\"Нижний Новгород Славянская 19 219\","
                        + "\"description\":\"\",\"logo_url\":\"https://household.key.rt.ru/api/v1/app/companies/9944/logo\","
                        + "\"name\":\"ООО ГАММА\",\"contacts\":[]}}}");

        CompanyPayload data = SnakeCaseDecoder.dataNode(ok, "company", CompanyPayload.class);

        assertThat(data.id()).isEqualTo(9944);
        assertThat(data.companyAddress()).isEqualTo("Нижний Новгород Славянская 19 219");
        assertThat(data.description()).isEmpty();
        assertThat(data.logoUrl()).isEqualTo("https://household.key.rt.ru/api/v1/app/companies/9944/logo");
        assertThat(data.name()).isEqualTo("ООО ГАММА");
    }

    @Test
    public void dataNodeDecodesObjectNode() {
        Response ok = new Response(200, "{\"data\":{\"key\":{\"id\":1,\"name\":\"home\",\"flat_id\":5}}}");

        KeyPayload payload = SnakeCaseDecoder.dataNode(ok, "key", KeyPayload.class);

        assertThat(payload.id()).isEqualTo(1);
        assertThat(payload.name()).isEqualTo("home");
        assertThat(payload.flatId()).isEqualTo(5);
    }

    @Test
    public void dataNodeIgnoresUnknownProperties() {
        Response ok = new Response(200, "{\"data\":{\"key\":{\"id\":1,\"name\":\"home\",\"futureField\":1}}}");

        KeyPayload payload = SnakeCaseDecoder.dataNode(ok, "key", KeyPayload.class);

        assertThat(payload.id()).isEqualTo(1);
        assertThat(payload.name()).isEqualTo("home");
    }

    @Test
    public void dataNodeDecodesListNode() {
        Response ok = new Response(200,
                "{\"data\":{\"devices\":[{\"id\":1,\"name\":\"a\"},{\"id\":2,\"name\":\"b\"}]}}");
        TypeReference<List<KeyPayload>> list = new TypeReference<>() {
        };

        List<KeyPayload> payload = SnakeCaseDecoder.dataNode(ok, "devices", list);

        assertThat(payload).hasSize(2);
        assertThat(payload.get(0).id()).isEqualTo(1);
        assertThat(payload.get(1).name()).isEqualTo("b");
    }

    @Test
    public void dataNodeDecodesEmptyListNode() {
        Response ok = new Response(200, "{\"data\":{\"devices\":[]}}");
        TypeReference<List<KeyPayload>> list = new TypeReference<>() {
        };

        assertThat(SnakeCaseDecoder.dataNode(ok, "devices", list)).isEmpty();
    }

    @Test
    public void dataNodeThrowsWhenDataMissingOrNull() {
        TypeReference<KeyPayload> type = new TypeReference<>() {
        };

        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(new Response(200, "{}"), "key", type))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data is missing or null");
        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(new Response(200, "{\"data\":null}"), "key", type))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data is missing or null");
    }

    @Test
    public void dataNodeThrowsWhenDataNotObject() {
        TypeReference<KeyPayload> type = new TypeReference<>() {
        };

        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(new Response(200, "{\"data\":[]}"), "key", type))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data is not an object");
        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(new Response(200, "{\"data\":\"x\"}"), "key", type))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data is not an object");
    }

    @Test
    public void dataNodeThrowsWhenNodeMissing() {
        Response ok = new Response(200, "{\"data\":{\"other\":{}}}");

        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(ok, "key", KeyPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data.key is missing or null");
    }

    @Test
    public void dataNodeThrowsWhenNodeNull() {
        Response ok = new Response(200, "{\"data\":{\"key\":null}}");

        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(ok, "key", KeyPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("data.key is missing or null");
    }

    @Test
    public void dataNodeThrowsOnPayloadMismatch() {
        Response ok = new Response(200, "{\"data\":{\"key\":\"not-an-object\"}}");

        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(ok, "key", KeyPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("does not match");
    }

    @Test
    public void dataNodeThrowsOnEmptyBody() {
        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(new Response(200, ""), "key", KeyPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("has no body");
    }

    @Test
    public void dataNodeThrowsOnNonJsonOrNonObjectRoot() {
        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(new Response(200, "not json"), "key", KeyPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not JSON");
        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(new Response(200, "[1,2]"), "key", KeyPayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not a JSON object");
    }

    @Test
    public void dataNodeThrowsApiExceptionOnErrorStatus() {
        Response err = new Response(422,
                "{\"error\":{\"message\":\"boom\",\"reasons\":[{\"type\":\"VALIDATION\"}]}}");

        assertThatThrownBy(() -> SnakeCaseDecoder.dataNode(err, "key", KeyPayload.class))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessageContaining("boom");
    }

    @Test
    public void rootNodeDecodesObjectNode() {
        Response ok = new Response(200, "{\"device\":{\"id\":9,\"name\":\"domofon\"}}");

        DevicePayload payload = SnakeCaseDecoder.rootNode(ok, "device", DevicePayload.class);

        assertThat(payload.id()).isEqualTo(9);
        assertThat(payload.name()).isEqualTo("domofon");
    }

    @Test
    public void rootNodeDecodesListNode() {
        Response ok = new Response(200,
                "{\"events\":[{\"device_id\":7,\"event_name\":\"open\"},{\"device_id\":8,\"event_name\":\"close\"}]}");
        TypeReference<List<EventPayload>> list = new TypeReference<>() {
        };

        List<EventPayload> payload = SnakeCaseDecoder.rootNode(ok, "events", list);

        assertThat(payload).hasSize(2);
        assertThat(payload.get(0).deviceId()).isEqualTo(7);
        assertThat(payload.get(1).eventName()).isEqualTo("close");
    }

    @Test
    public void rootNodeThrowsWhenNodeMissing() {
        Response ok = new Response(200, "{\"other\":{}}");

        assertThatThrownBy(() -> SnakeCaseDecoder.rootNode(ok, "events", DevicePayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("root.events is missing or null");
    }

    @Test
    public void rootNodeThrowsWhenNodeNull() {
        Response ok = new Response(200, "{\"events\":null}");

        assertThatThrownBy(() -> SnakeCaseDecoder.rootNode(ok, "events", DevicePayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("root.events is missing or null");
    }

    @Test
    public void rootNodeThrowsOnNodeMismatch() {
        Response ok = new Response(200, "{\"events\":\"not-an-object\"}");

        assertThatThrownBy(() -> SnakeCaseDecoder.rootNode(ok, "events", DevicePayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("does not match");
    }

    @Test
    public void rootNodeThrowsOnEmptyBody() {
        assertThatThrownBy(() -> SnakeCaseDecoder.rootNode(new Response(200, ""), "events", DevicePayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("has no body");
    }

    @Test
    public void rootNodeThrowsOnNonJsonOrNonObjectRoot() {
        assertThatThrownBy(() -> SnakeCaseDecoder.rootNode(new Response(200, "not json"), "events", DevicePayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not JSON");
        assertThatThrownBy(() -> SnakeCaseDecoder.rootNode(new Response(200, "[1,2]"), "events", DevicePayload.class))
                .isInstanceOf(RtKeyContractException.class)
                .hasMessageContaining("is not a JSON object");
    }

    @Test
    public void rootNodeThrowsApiExceptionOnErrorStatus() {
        Response err = new Response(422,
                "{\"error\":{\"message\":\"boom\",\"reasons\":[{\"type\":\"VALIDATION\"}]}}");

        assertThatThrownBy(() -> SnakeCaseDecoder.rootNode(err, "events", DevicePayload.class))
                .isInstanceOf(RtKeyApiException.class)
                .hasMessageContaining("boom");
    }
}
