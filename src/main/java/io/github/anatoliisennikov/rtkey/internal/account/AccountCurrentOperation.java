package io.github.anatoliisennikov.rtkey.internal.account;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.User;

import java.util.function.Supplier;

final class AccountCurrentOperation {
    private static final String PATH = "/api/v3/app/users/current";

    private AccountCurrentOperation() {

    }

    public static User call(HttpTransport transport, Supplier<String> tokenSupplier) {
        Response response = transport.get(Host.HOUSEHOLD, PATH, tokenSupplier);
        UserData userData = SnakeCaseDecoder.data(response, UserData.class);

        return UserData.toModel(userData);
    }
}
