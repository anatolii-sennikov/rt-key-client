package io.github.anatoliisennikov.rtkey.model;

/**
 * A management company as returned by
 * {@link io.github.anatoliisennikov.rtkey.AccountService#company(int)}.
 *
 * @param id          company identifier
 * @param address     company address
 * @param description company description
 * @param name        company name
 */
public record Company(
        Integer id,
        String address,
        String description,
        String name
) {
}
