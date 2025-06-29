package chloe.movietalk.exception.auth;

import chloe.movietalk.exception.CustomException;

public class InvalidPasswordException extends CustomException {
    public static final CustomException EXCEPTION = new InvalidPasswordException();

    private InvalidPasswordException() {
        super(AuthErrorCode.INVALID_PASSWORD);
    }
}
