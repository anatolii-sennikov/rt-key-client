package io.github.anatoliisennikov.rtkey.internal.account;

import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.codec.SnakeCaseDecoder;
import io.github.anatoliisennikov.rtkey.internal.transport.HttpTransport;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.Company;

import java.util.function.Supplier;

final class CompanyOperation {
    private static final String PATH = "/api/v1/app/company";

    private CompanyOperation() {

    }

    public static Company call(HttpTransport transport, Integer flatId, Supplier<String> tokenSupplier) {
        Response response = transport.get(Host.HOUSEHOLD, PATH, Query.of("flat_id", String.valueOf(flatId)), tokenSupplier);
        CompanyData companyData = SnakeCaseDecoder.dataNode(response, "company", CompanyData.class);
        return CompanyData.toModel(companyData);
    }
}
