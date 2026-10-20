package io.github.anatoliisennikov.rtkey.model;

import java.time.Instant;

/**
 * An authentication session returned by
 * {@link io.github.anatoliisennikov.rtkey.RtKeyAuth#login(CodeDispatch, String)}.
 *
 * <p>{@link #toString()} does not reveal the access token.
 *
 * @param accessToken access token sent with authenticated requests
 * @param expiredAt   time the session expires
 */
public record Session(String accessToken, Instant expiredAt) {
    @Override
    public String toString() {
        return "Session[redacted]";
    }
}
