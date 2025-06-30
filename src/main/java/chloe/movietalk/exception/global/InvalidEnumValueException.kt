package chloe.movietalk.exception.global;

import chloe.movietalk.exception.CustomException;

public class InvalidEnumValueException extends CustomException {

    public static final CustomException EXCEPTION = new InvalidEnumValueException();

    private InvalidEnumValueException() {
        super(GlobalErrorCode.INVALID_ENUM_VALUE);
    }
}
