package chloe.movietalk.exception.auth;

import chloe.movietalk.exception.CustomException;

public class UserNotFoundException extends CustomException {
    public static final CustomException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(AuthErrorCode.USER_NOT_FOUND);
    }
}
