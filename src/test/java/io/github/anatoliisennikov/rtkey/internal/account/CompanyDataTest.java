package io.github.anatoliisennikov.rtkey.internal.account;

import io.github.anatoliisennikov.rtkey.model.Company;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyDataTest {

    @Test
    public void toModelMapsEveryPublicField() {
        Company company = CompanyData.toModel(companyData("https://key.rt.ru/logo.png", List.of("+74950000001")));

        assertThat(company).isEqualTo(new Company(
                4321,
                "Москва, ул. Тверская, 1",
                "Управляющая компания",
                "РТ-Дом"
        ));
    }

    @Test
    public void toModelRenamesCompanyAddressToAddress() {
        Company company = CompanyData.toModel(companyData("https://key.rt.ru/logo.png", List.of()));

        assertThat(company.address()).isEqualTo("Москва, ул. Тверская, 1");
        assertThat(company.description()).isEqualTo("Управляющая компания");
        assertThat(company.name()).isEqualTo("РТ-Дом");
    }

    @Test
    public void toModelIgnoresLogoUrlAndContacts() {
        Company withLogoAndContacts =
                CompanyData.toModel(companyData("https://key.rt.ru/logo.png", List.of("+74950000001")));
        Company withOtherLogoAndContacts =
                CompanyData.toModel(companyData("https://key.rt.ru/other-logo.png", List.of("+74950000002")));

        assertThat(withLogoAndContacts).isEqualTo(withOtherLogoAndContacts);
    }

    @Test
    public void toModelKeepsIdFromTheDto() {
        Company company = CompanyData.toModel(companyData("https://key.rt.ru/logo.png", List.of()));

        assertThat(company.id()).isEqualTo(4321);
    }

    private static CompanyData companyData(String logoUrl, List<Object> contacts) {
        return new CompanyData(
                4321,
                "Москва, ул. Тверская, 1",
                "Управляющая компания",
                logoUrl,
                "РТ-Дом",
                contacts
        );
    }
}
