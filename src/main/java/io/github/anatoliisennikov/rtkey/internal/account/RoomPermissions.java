package io.github.anatoliisennikov.rtkey.internal.account;

record RoomPermissions(
        Boolean barriersFullCodeGenerateBehavior,
        Boolean devicesCallRedirectToMobileAppBehavior,
        Boolean devicesCallRedirectToPstnBehavior,
        Boolean devicesCallRedirectToSipBehavior,
        Boolean devicesCallToCmsPhonesBehavior,
        Boolean devicesRfidsAccessControlPanelAccessBehavior,
        Boolean devicesRfidsBarrierAccessBehavior,
        Boolean devicesRfidsIntercomAccessBehavior,
        Boolean frIntercomAccessBehavior
) {
}
