package io.github.anatoliisennikov.rtkey.internal.account;


import io.github.anatoliisennikov.rtkey.AccountService;

import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.Company;
import io.github.anatoliisennikov.rtkey.model.User;

import java.util.function.Supplier;

public final class AccountServiceImpl implements AccountService {
    private final HttpTransport transport;
    private final Supplier<String> tokenSupplier;

    public AccountServiceImpl(HttpTransport transport, Supplier<String> tokenSupplier) {
        this.transport = transport;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public User user() {
        return AccountCurrentOperation.call(transport, tokenSupplier);
    }

    @Override
    public Company company(int flatId) {
        if (flatId <= 0) throw new IllegalArgumentException("flatId must be positive");
        return CompanyOperation.call(transport, flatId, tokenSupplier);
    }
}
