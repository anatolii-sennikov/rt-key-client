package io.github.anatoliisennikov.rtkey.internal.transport;

import io.github.anatoliisennikov.rtkey.Host;

import java.net.URI;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class Hosts {
    private static final String DEFAULT_HOUSEHOLD = "https://household.key.rt.ru";
    private static final String DEFAULT_KEYAPIS = "https://keyapis.key.rt.ru";
    private static final String DEFAULT_VC = "https://vc.key.rt.ru";
    private static final String DEFAULT_EVENTS = "https://events.key.rt.ru";

    private final EnumMap<Host, URI> urls;

    private Hosts(EnumMap<Host, URI> urls) {
        this.urls = urls;
    }

    public static Hosts production() {
        return withOverrides(Map.of());
    }

    public URI url(Host host) {
        Objects.requireNonNull(host, "host");
        return Objects.requireNonNull(urls.get(host), () -> "no URL for " + host);
    }


    public static Hosts withOverrides(Map<Host, URI> overrides) {
        Objects.requireNonNull(overrides, "overrides");
        EnumMap<Host, URI> urls = defaults();
        overrides.forEach((host, uri) ->
                urls.put(Objects.requireNonNull(host, "host"), checkBaseUri(uri)));
        return new Hosts(urls);
    }

    public static URI checkBaseUri(URI baseUri) {
        Objects.requireNonNull(baseUri, "baseUri");
        if (!baseUri.isAbsolute()) {
            throw new IllegalArgumentException("baseUri must be absolute: " + baseUri);
        }
        String scheme = baseUri.getScheme();
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("baseUri must use http or https: " + baseUri);
        }
        if (baseUri.getRawQuery() != null || baseUri.getRawFragment() != null) {
            throw new IllegalArgumentException("baseUri must not contain query or fragment: " + baseUri);
        }
        String path = baseUri.getRawPath();
        if (path != null && !path.isEmpty() && !"/".equals(path)) {
            throw new IllegalArgumentException("baseUri must not contain a path prefix: "
                    + baseUri);
        }
        return baseUri;
    }

    private static EnumMap<Host, URI> defaults() {
        return new EnumMap<>(Map.of(
                Host.HOUSEHOLD, URI.create(DEFAULT_HOUSEHOLD),
                Host.KEYAPIS, URI.create(DEFAULT_KEYAPIS),
                Host.VC, URI.create(DEFAULT_VC),
                Host.EVENTS, URI.create(DEFAULT_EVENTS)));
    }

    public URI resolve(Host host, String path, Query query) {
        Objects.requireNonNull(host, "host");
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(urls.get(host), "no URL for " + host);

        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("path must start with /");
        }

        URI base = urls.get(host).resolve(checkedPath(path));

        if (query.isEmpty()) {
            return base;
        }

        try {
            return URI.create(base.getScheme() + "://" + base.getRawAuthority() + base.getRawPath() + "?" + query.encoded());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Cannot build URI from " + base + " and query " + query.encoded(), e);
        }
    }

    private static String checkedPath(String path) {
        Objects.requireNonNull(path, "path");
        if (path.indexOf('?') >= 0 || path.indexOf('#') >= 0) {
            throw new IllegalArgumentException("path must not contain ? or #, query goes through Query");
        }
        return path;
    }
}
