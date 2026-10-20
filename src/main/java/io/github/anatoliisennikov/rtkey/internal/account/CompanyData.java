package io.github.anatoliisennikov.rtkey.internal.account;

import java.util.List;
import io.github.anatoliisennikov.rtkey.model.Company;

record CompanyData(
        Integer id,
        String companyAddress,
        String description,
        String logoUrl,
        String name,
        List<Object> contacts
) {
    static Company toModel(CompanyData data) {
        return new Company(data.id(), data.companyAddress(), data.description(), data.name());
    }
}
