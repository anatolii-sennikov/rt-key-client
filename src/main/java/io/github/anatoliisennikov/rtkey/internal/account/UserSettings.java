package io.github.anatoliisennikov.rtkey.internal.account;

record UserSettings(
        Boolean redirectToMobileApplication,
        Boolean devicesCallRedirectToPstn,
        Boolean devicesCallRedirectToSip
) {
}