package chloe.movietalk.exception.global;

import chloe.movietalk.exception.CustomException;

public class InvalidRoleEnumValueException extends CustomException {

    public static final CustomException EXCEPTION = new InvalidRoleEnumValueException();

    private InvalidRoleEnumValueException() {
        super(GlobalErrorCode.INVALID_ROLE_ENUM_VALUE);
    }
}
