package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.model.*;

import java.util.List;

/**
 * Door code operations: door codes, emergency codes and the flat intercode.
 *
 * <p>Obtained from {@link RtKeyClient#codes()}.
 *
 * <p>Every method sends an authenticated request through the client that created this service.
 * Failures are reported as {@link io.github.anatoliisennikov.rtkey.exception.RtKeyApiException},
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException} or
 * {@link io.github.anatoliisennikov.rtkey.exception.RtKeyContractException}; calling a method
 * after the client is closed throws {@link IllegalStateException}.
 */
public interface CodeService {
    /**
     * Returns a page of door codes.
     *
     * @return the page of door codes
     */
    Page<Code> codes();

    /**
     * Returns a page of door codes of the given type.
     *
     * @param codeType the code type
     * @return the page of door codes
     * @throws NullPointerException if {@code codeType} is {@code null}
     */
    Page<Code> codes(CodeType codeType);

    /**
     * Generates a temporary door code for a flat and a device.
     *
     * @param flatId   the flat identifier
     * @param deviceId the device identifier
     * @return the generated code
     * @throws IllegalArgumentException if {@code flatId} or {@code deviceId} is not positive
     */
    GeneratedCode generateCode(int flatId, int deviceId);

    /**
     * Returns the emergency codes of a device.
     *
     * @param deviceId the device identifier
     * @return the emergency codes
     * @throws IllegalArgumentException if {@code deviceId} is not positive
     */
    List<EmergencyCode> emergencyCodes(int deviceId);

    /**
     * Returns the intercode of a flat.
     *
     * @param flatId the flat identifier
     * @return the intercode
     * @throws IllegalArgumentException if {@code flatId} is not positive
     */
    Intercode intercode(int flatId);

    /**
     * Deletes the intercode of a flat.
     *
     * @param flatId the flat identifier
     * @throws IllegalArgumentException if {@code flatId} is not positive
     */
    void deleteIntercode(int flatId);
}
