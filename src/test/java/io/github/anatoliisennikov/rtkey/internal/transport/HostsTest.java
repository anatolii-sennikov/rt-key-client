package io.github.anatoliisennikov.rtkey.internal.transport;

import io.github.anatoliisennikov.rtkey.Host;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HostsTest {

    @Test
    public void production() {
        Hosts hosts = Hosts.production();
        assertThat(hosts.url(Host.HOUSEHOLD).toString()).isEqualTo("https://household.key.rt.ru");
        assertThat(hosts.url(Host.KEYAPIS).toString()).isEqualTo("https://keyapis.key.rt.ru");
        assertThat(hosts.url(Host.EVENTS).toString()).isEqualTo("https://events.key.rt.ru");
        assertThat(hosts.url(Host.VC).toString()).isEqualTo("https://vc.key.rt.ru");
    }

    @Test
    public void overrides() {
        Hosts hosts = Hosts.withOverrides(Map.of(Host.HOUSEHOLD, URI.create("https://override.test.ru"),
                Host.VC, URI.create("https://override.com")));

        assertThat(hosts.url(Host.HOUSEHOLD).toString()).isEqualTo("https://override.test.ru");
        assertThat(hosts.url(Host.KEYAPIS).toString()).isEqualTo("https://keyapis.key.rt.ru");
        assertThat(hosts.url(Host.EVENTS).toString()).isEqualTo("https://events.key.rt.ru");
        assertThat(hosts.url(Host.VC).toString()).isEqualTo("https://override.com");
    }

    @Test
    public void mustBeAbsolute() {
        assertThatThrownBy(() -> Hosts.checkBaseUri(URI.create("vc.key.rt.ru")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("baseUri must be absolute: vc.key.rt.ru");
    }

    @Test
    public void mustUseHttpOrHttps() {
        assertThatThrownBy(() -> Hosts.checkBaseUri(URI.create("ftp://vc.key.rt.ru")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("baseUri must use http or https: ftp://vc.key.rt.ru");
    }

    @Test
    public void mustNotContainPathPrefix() {
        assertThatThrownBy(() -> Hosts.checkBaseUri(URI.create("https://keyapis.key.rt.ru/somepath/")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("baseUri must not contain a path prefix: https://keyapis.key.rt.ru/somepath/");
    }

    @Test
    public void mustNotContainQueryFragment() {
        assertThatThrownBy(() -> Hosts.checkBaseUri(URI.create("https://keyapis.key.rt.ru/?param=value")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("baseUri must not contain query or fragment: https://keyapis.key.rt.ru/?param=value");
    }

    @Test
    public void pathMustStartWithSlash() {
        Hosts hosts = Hosts.production();
        assertThatThrownBy(() -> hosts.resolve(Host.HOUSEHOLD, "somepath", Query.empty()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("path must start with /");
    }

    @Test
    public void resolveProductionUrl() {
        Hosts hosts = Hosts.production();
        URI host = hosts.url(Host.HOUSEHOLD);
        assertThat(host).isEqualTo(URI.create("https://household.key.rt.ru"));
    }

    @Test
    public void resolveAppendsPath() {
        Hosts hosts = Hosts.production();
        URI host = hosts.resolve(Host.HOUSEHOLD, "/v1/somepath", Query.empty());
        assertThat(host).isEqualTo(URI.create("https://household.key.rt.ru/v1/somepath"));
    }

    @Test
    public void resolveAppendsEncodedQuery() {
        Hosts hosts = Hosts.production();
        URI uri = hosts.resolve(Host.HOUSEHOLD, "/v1/x", Query.of("id", "a b"));
        assertThat(uri).isEqualTo(URI.create("https://household.key.rt.ru/v1/x?id=a%20b"));
        assertThat(uri.getQuery()).isEqualTo("id=a b");
    }

    @Test
    public void resolveRejectsQuestionMarkInPath() {
        assertThatThrownBy(() -> Hosts.production().resolve(Host.HOUSEHOLD, "/v1?x=1", Query.empty()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("path must not contain ? or #, query goes through Query");
    }

    @Test
    public void resolveRejectsHashInPath() {
        assertThatThrownBy(() -> Hosts.production().resolve(Host.HOUSEHOLD, "/v1#f", Query.empty()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("path must not contain ? or #, query goes through Query");
    }

    @Test
    public void checkBaseUriAcceptsRootPath() {
        assertThat(Hosts.checkBaseUri(URI.create("https://h/"))).isEqualTo(URI.create("https://h/"));
    }
}
