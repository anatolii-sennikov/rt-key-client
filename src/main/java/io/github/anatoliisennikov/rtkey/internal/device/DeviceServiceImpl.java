package io.github.anatoliisennikov.rtkey.internal.device;

import io.github.anatoliisennikov.rtkey.DeviceService;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.Device;
import io.github.anatoliisennikov.rtkey.model.Intercom;
import io.github.anatoliisennikov.rtkey.model.Page;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class DeviceServiceImpl implements DeviceService {
    private final HttpTransport transport;
    private final Supplier<String> tokenSupplier;

    public DeviceServiceImpl(HttpTransport transport, Supplier<String> tokenSupplier) {
        this.transport = transport;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public Page<Device> devices() {
        return DeviceOperation.call(transport, tokenSupplier, null);
    }

    @Override
    public Page<Device> devices(int id) {
        if (id <= 0) throw new IllegalArgumentException("id must be positive");
        return DeviceOperation.call(transport, tokenSupplier, id);
    }

    @Override
    public List<Intercom> intercoms() {
        return IntercomsOperation.call(transport, tokenSupplier);
    }

    @Override
    public Intercom renameIntercom(int id, String name) {
        Objects.requireNonNull(name, "name must not be null");
        if (id <= 0) throw new IllegalArgumentException("id must be positive");
        return RenameOperation.call(transport, tokenSupplier, String.valueOf(id), name);
    }

    @Override
    public void open(int id) {
        if (id <= 0) throw new IllegalArgumentException("id must be positive");
        OpenOperation.call(transport, tokenSupplier, String.valueOf(id));
    }
}
