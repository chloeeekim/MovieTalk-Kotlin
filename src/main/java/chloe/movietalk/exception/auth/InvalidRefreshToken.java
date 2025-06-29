package chloe.movietalk.exception.auth;

import chloe.movietalk.exception.CustomException;

public class InvalidRefreshToken extends CustomException {
    public static final CustomException EXCEPTION = new InvalidRefreshToken();

    private InvalidRefreshToken() {
        super(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }
}
