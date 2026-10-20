package io.github.anatoliisennikov.rtkey.model;

import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * An entry of the events journal, returned by
 * {@link io.github.anatoliisennikov.rtkey.EventService#events(java.time.Instant, java.time.Instant)}.
 *
 * <p>{@link #toString()} redacts {@code login}, {@code password} and {@code pushToken}.
 *
 * @param raisedAt           time the event was raised
 * @param eventType          event type reported by the server; the values accepted by the events
 *                           query are listed in {@link EventTypes}
 * @param deviceId           device identifier, when set
 * @param deviceType         device type reported by the server, when set
 * @param companyId          company identifier, when set
 * @param roomId             flat identifier ({@code flats[].id}), when set
 * @param relay              reported by the server, when set; passed through without interpretation
 * @param userId             user identifier
 * @param userAgent          user agent reported in the event
 * @param buildingId         building identifier, when set
 * @param orponId            ORPON identifier, when set
 * @param roomNumber         flat number, when set
 * @param ivrPhoneNumber     reported by the server, when set; passed through without interpretation
 * @param guestPhoneNumber   reported by the server, when set; passed through without interpretation
 * @param code               code reported by the server, when set
 * @param rfid               RFID value reported by the server, when set
 * @param requestId          request identifier assigned by the server
 * @param dtmfPhoneNumber    reported by the server, when set; passed through without interpretation
 * @param deviceUserType     reported by the server, when set; passed through without interpretation
 * @param login              login reported in the event, when set
 * @param password           password reported in the event, when set
 * @param host               host reported in the event, when set
 * @param deviceModel        device model reported in the event, when set
 * @param os                 operating system reported in the event, when set
 * @param pushToken          push token reported in the event, when set
 * @param codeId             identifier of the code related to the event, when set
 * @param rfidId             identifier of the RFID related to the event, when set
 * @param cameraUid          camera UID related to the event, when set
 * @param screenshotUrl      screenshot URL related to the event, when set
 * @param carNumber          car number, when set
 * @param vehicle            reported by the server, when set; passed through without interpretation
 * @param courierCompanyName courier company name, when set
 * @param orderNumber        order number, when set
 * @param courierId          courier identifier, when set
 */
public record Event(
        Instant raisedAt,
        String eventType,
        @Nullable
        Integer deviceId,
        @Nullable
        String deviceType,
        @Nullable
        Integer companyId,
        @Nullable
        Integer roomId,
        @Nullable
        Integer relay,
        Integer userId,
        String userAgent,
        @Nullable
        Integer buildingId,
        @Nullable
        Long orponId,
        @Nullable
        String roomNumber,
        @Nullable
        String ivrPhoneNumber,
        @Nullable
        String guestPhoneNumber,
        @Nullable
        String code,
        @Nullable
        String rfid,
        String requestId,
        @Nullable
        String dtmfPhoneNumber,
        @Nullable
        String deviceUserType,
        @Nullable
        String login,
        @Nullable
        String password,
        @Nullable
        String host,
        @Nullable
        String deviceModel,
        @Nullable
        String os,
        @Nullable
        String pushToken,
        @Nullable
        Integer codeId,
        @Nullable
        Integer rfidId,
        @Nullable
        String cameraUid,
        @Nullable
        String screenshotUrl,
        @Nullable
        String carNumber,
        @Nullable
        String vehicle,
        @Nullable
        String courierCompanyName,
        @Nullable
        String orderNumber,
        @Nullable
        String courierId
) {
        @Override
        public String toString() {
                return "Event{" +
                        "raisedAt=" + raisedAt +
                        ", eventType='" + eventType + '\'' +
                        ", deviceId=" + deviceId +
                        ", deviceType='" + deviceType + '\'' +
                        ", companyId=" + companyId +
                        ", roomId=" + roomId +
                        ", relay=" + relay +
                        ", userId=" + userId +
                        ", userAgent='" + userAgent + '\'' +
                        ", buildingId=" + buildingId +
                        ", orponId=" + orponId +
                        ", roomNumber='" + roomNumber + '\'' +
                        ", ivrPhoneNumber='" + ivrPhoneNumber + '\'' +
                        ", guestPhoneNumber='" + guestPhoneNumber + '\'' +
                        ", code='" + code + '\'' +
                        ", rfid='" + rfid + '\'' +
                        ", requestId='" + requestId + '\'' +
                        ", dtmfPhoneNumber='" + dtmfPhoneNumber + '\'' +
                        ", deviceUserType='" + deviceUserType + '\'' +
                        ", login='login is hidden by security policy'" +
                        ", password='password is hidden by security policy'" +
                        ", host='" + host + '\'' +
                        ", deviceModel='" + deviceModel + '\'' +
                        ", os='" + os + '\'' +
                        ", pushToken='pushToken is hidden by security policy'" +
                        ", codeId=" + codeId +
                        ", rfidId=" + rfidId +
                        ", cameraUid='" + cameraUid + '\'' +
                        ", screenshotUrl='" + screenshotUrl + '\'' +
                        ", carNumber='" + carNumber + '\'' +
                        ", vehicle='" + vehicle + '\'' +
                        ", courierCompanyName='" + courierCompanyName + '\'' +
                        ", orderNumber='" + orderNumber + '\'' +
                        ", courierId='" + courierId + '\'' +
                        '}';
        }
}
