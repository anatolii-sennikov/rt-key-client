package io.github.anatoliisennikov.rtkey.model;

import java.util.List;

/**
 * The current user as returned by {@link io.github.anatoliisennikov.rtkey.AccountService#user()}.
 *
 * @param userId              user identifier
 * @param login               login
 * @param phoneNumber         phone number
 * @param status              user status reported by the server; a server-defined code, not mapped
 *                            to an enum
 * @param phoneNumberVerified whether the phone number is verified
 * @param emailVerified       whether the email is verified
 * @param company             company the user belongs to
 * @param flats               flats available to the user
 */
public record User (
        Integer userId,
        String login,
        String phoneNumber,
        String status,
        Boolean phoneNumberVerified,
        Boolean emailVerified,
        Company company,
        List<Flat> flats
) {
    /**
     * Company the user belongs to.
     *
     * @param id    company identifier
     * @param title company title
     */
    public record Company(
            Integer id,
            String title
    ) {

    }
}
