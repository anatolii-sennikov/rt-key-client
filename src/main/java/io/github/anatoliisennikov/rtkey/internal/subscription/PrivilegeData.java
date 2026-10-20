package io.github.anatoliisennikov.rtkey.internal.subscription;

import io.github.anatoliisennikov.rtkey.model.Privilege;

record PrivilegeData(
        String orpon,
        String flatNumber,
        String type,
        String stateType
) {
    static Privilege toModel(PrivilegeData data) {
        return new Privilege(
                data.orpon(),
                data.flatNumber(),
                data.type(),
                data.stateType()
        );
    }
}
