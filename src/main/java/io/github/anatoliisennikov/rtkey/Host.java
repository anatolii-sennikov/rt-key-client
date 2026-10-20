package io.github.anatoliisennikov.rtkey;

/**
 * RT Key API host endpoints.
 *
 * <p>Each constant identifies a base URI used by the transport layer. Base URIs can be
 * overridden per client: {@link RtKeyClient.Builder#baseUri(Host, java.net.URI)} accepts
 * any host, while {@link RtKeyAuth.Builder#baseUri(Host, java.net.URI)} accepts only
 * {@link #KEYAPIS}.
 */
public enum Host {
    /** https://household.key.rt.ru */
    HOUSEHOLD,
    /** https://keyapis.key.rt.ru */
    KEYAPIS,
    /** https://vc.key.rt.ru */
    VC,
    /** https://events.key.rt.ru */
    EVENTS;
}
