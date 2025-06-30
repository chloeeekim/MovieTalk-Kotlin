package chloe.movietalk.exception.auth;

import chloe.movietalk.exception.CustomException;

public class InvalidAccessToken extends CustomException {
    public static final CustomException EXCEPTION = new InvalidAccessToken();

    private InvalidAccessToken() {
        super(AuthErrorCode.INVALID_ACCESS_TOKEN);
    }
}
