package io.github.anatoliisennikov.rtkey;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.github.anatoliisennikov.rtkey.model.CodeDispatch;
import io.github.anatoliisennikov.rtkey.model.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

public class RtKeyAuthE2ETest {

    private static WireMockServer keyapis;
    private static RtKeyAuth auth;

    @BeforeAll
    static void startServers() {
        keyapis = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        keyapis.start();

        auth = RtKeyAuth.builder()
                .deviceId(UUID.fromString("3e3a4f5b-6666-7777-8888-999900001111"))
                .baseUri(Host.KEYAPIS, URI.create("http://127.0.0.1:" + keyapis.port() + "/"))
                .build();
    }

    @AfterAll
    static void stopServers() {
        auth.close();
        keyapis.stop();
    }

    @BeforeEach
    void reset() {
        keyapis.resetAll();
    }

    @Test
    public void sendCodeReachesKeyapisWithDeviceId() {
        keyapis.stubFor(post(urlPathEqualTo("/identity/api/v1/authorization/send_code"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody("{\"data\":{\"codeId\":\"cid\",\"timeout\":120}}")));

        CodeDispatch cd = auth.sendCode("+7999");

        assertThat(cd).isEqualTo(new CodeDispatch("cid", 120));
        keyapis.verify(postRequestedFor(urlPathEqualTo("/identity/api/v1/authorization/send_code"))
                .withHeader("X-Device-Id", equalTo("3e3a4f5b-6666-7777-8888-999900001111")));
    }

    @Test
    public void loginReachesKeyapisWithDeviceId() {
        keyapis.stubFor(post(urlPathEqualTo("/identity/api/v1/authorization/login"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody("{\"data\":{\"accessToken\":\"jwt\",\"expiredAt\":\"2027-01-01T00:00:00Z\"}}")));

        CodeDispatch cd = new CodeDispatch("codeId-123", 120);
        Session session = auth.login(cd, "1234");

        assertThat(session).isEqualTo(new Session("jwt", Instant.parse("2027-01-01T00:00:00Z")));
        keyapis.verify(postRequestedFor(urlPathEqualTo("/identity/api/v1/authorization/login"))
                .withHeader("X-Device-Id", equalTo("3e3a4f5b-6666-7777-8888-999900001111")));
    }
}
