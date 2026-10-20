package io.github.anatoliisennikov.rtkey.model;

/**
 * The dispatch of a verification code: the result of the first authentication step.
 *
 * <p>Returned by {@link io.github.anatoliisennikov.rtkey.RtKeyAuth#sendCode(String)} and passed to
 * {@link io.github.anatoliisennikov.rtkey.RtKeyAuth#login(CodeDispatch, String)}.
 *
 * @param codeId  identifier of the dispatched code, required by {@code login}
 * @param timeout timeout reported by the server; in server-defined units
 */
public record CodeDispatch(String codeId, Integer timeout) {
}
