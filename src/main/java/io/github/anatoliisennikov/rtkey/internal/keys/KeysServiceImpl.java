package io.github.anatoliisennikov.rtkey.internal.keys;

import io.github.anatoliisennikov.rtkey.KeysService;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.Key;
import io.github.anatoliisennikov.rtkey.model.LimitOffsetQuery;
import io.github.anatoliisennikov.rtkey.model.Page;
import io.github.anatoliisennikov.rtkey.model.Rfid;

import java.util.Objects;
import java.util.function.Supplier;

public final class KeysServiceImpl implements KeysService {
    private final HttpTransport transport;
    private final Supplier<String> tokenSupplier;

    public KeysServiceImpl(HttpTransport transport, Supplier<String> tokenSupplier) {
        this.transport = transport;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public Page<Rfid> rfids() {
        return RfidsOperation.call(transport, tokenSupplier, LimitOffsetQuery.empty());
    }

    @Override
    public Page<Rfid> rfids(LimitOffsetQuery query) {
        Objects.requireNonNull(query, "query must not be null");
        return RfidsOperation.call(transport, tokenSupplier, query);
    }

    @Override
    public Key createKey(int flatId, String rfidUid, String name) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(rfidUid, "rfidUid must not be null");
        if (flatId <= 0) throw new IllegalArgumentException("flatId must be positive");
        return KeysOperation.createKey(transport, tokenSupplier, flatId, rfidUid, name);
    }

    @Override
    public Key renameKey(int keyId, String name) {
        Objects.requireNonNull(name, "name must not be null");
        if (keyId <= 0) throw new IllegalArgumentException("keyId must be positive");
        return KeysOperation.changeKeyName(transport, tokenSupplier, keyId, name);
    }

    @Override
    public void deleteRfid(int rfidId) {
        if (rfidId <= 0) throw new IllegalArgumentException("rfidId must be positive");
        RfidsOperation.deleteRfid(transport, tokenSupplier, rfidId);
    }
}
