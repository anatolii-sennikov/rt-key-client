package io.github.anatoliisennikov.rtkey.internal.device;


import java.time.Instant;

record EmergencyDoor(
        String statusType,
        String finalStatusType,
        Instant statusAt,
        String statusError,
        Integer statusRetryCount
) {
}
