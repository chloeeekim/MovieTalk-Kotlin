package chloe.movietalk.exception.auth;

import chloe.movietalk.exception.CustomException;

public class AlreadyExistsUserException extends CustomException {
    public static final CustomException EXCEPTION = new AlreadyExistsUserException();

    private AlreadyExistsUserException() {
        super(AuthErrorCode.ALREADY_EXISTS_USER);
    }
}
