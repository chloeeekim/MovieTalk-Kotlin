package chloe.movietalk.exception.auth;

import chloe.movietalk.exception.CustomException;

public class LoginRequiredException extends CustomException {
    public static final CustomException EXCEPTION = new LoginRequiredException();

    private LoginRequiredException() {
        super(AuthErrorCode.LOGIN_REQUIRED);
    }
}
