package io.github.anatoliisennikov.rtkey.internal.code;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Code;
import io.github.anatoliisennikov.rtkey.model.CodeType;
import io.github.anatoliisennikov.rtkey.model.Page;
import org.jspecify.annotations.Nullable;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.function.Supplier;


final class CodeOperation {
    private static final String PATH = "/api/v3/app/devices/codes";

    private CodeOperation() {

    }

    public static Page<Code> call(HttpTransport transport, Supplier<String> tokenSupplier, @Nullable CodeType codeType) {
        Query query = codeType == null ? Query.empty() : Query.of("types", codeType.wire());

        Response response = transport.get(Host.HOUSEHOLD, PATH, query, tokenSupplier);
        Page<CodeData> codeDataPage = SnakeCaseDecoder.data(response, new TypeReference<>() {
        });

        List<Code> codes = codeDataPage.items().stream().map(CodeData::toModel).toList();

        return new Page<>(codes, codeDataPage.total());
    }
}
