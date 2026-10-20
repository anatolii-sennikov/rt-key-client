package io.github.anatoliisennikov.rtkey.internal.identity;

import java.time.Instant;

record LoginData(String accessToken, Instant expiredAt) {
}
