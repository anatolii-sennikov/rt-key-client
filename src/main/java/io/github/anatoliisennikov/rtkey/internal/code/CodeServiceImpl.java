package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.CodeService;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.model.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class CodeServiceImpl implements CodeService {
    private final HttpTransport transport;
    private final Supplier<String> tokenSupplier;

    public CodeServiceImpl(HttpTransport transport, Supplier<String> tokenSupplier) {
        this.transport = transport;
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public Page<Code> codes() {
        return CodeOperation.call(transport, tokenSupplier, null);
    }

    @Override
    public Page<Code> codes(CodeType codeType) {
        Objects.requireNonNull(codeType, "codeType must not be null");
        return CodeOperation.call(transport, tokenSupplier, codeType);
    }

    @Override
    public GeneratedCode generateCode(int flatId, int deviceId) {
        if (flatId <= 0) throw new IllegalArgumentException("flatId must be positive");
        if (deviceId <= 0) throw new IllegalArgumentException("deviceId must be positive");

        return GenerateCodeOperation.call(transport, tokenSupplier, flatId, deviceId);
    }

    @Override
    public List<EmergencyCode> emergencyCodes(int deviceId) {
        if (deviceId <= 0) throw new IllegalArgumentException("deviceId must be positive");
        return EmergencyCodeOperation.call(transport, tokenSupplier, deviceId);
    }

    @Override
    public Intercode intercode(int flatId) {
        if (flatId <= 0) throw new IllegalArgumentException("flatId must be positive");
        return IntercodeOperation.getIntercode(transport, tokenSupplier, flatId);
    }

    @Override
    public void deleteIntercode(int flatId) {
        if (flatId <= 0) throw new IllegalArgumentException("flatId must be positive");
        IntercodeOperation.deleteIntercode(transport, tokenSupplier, flatId);
    }
}
