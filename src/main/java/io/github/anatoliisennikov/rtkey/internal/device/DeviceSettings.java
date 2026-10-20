package io.github.anatoliisennikov.rtkey.internal.device;

record DeviceSettings(
        Object autocollect,
        Object code,
        Object door,
        Object dtmf,
        EmergencyDoor emergencyDoor,
        Object gate,
        Object ntp,
        Object rfid,
        Object sip,
        Object syslog
) {
}
