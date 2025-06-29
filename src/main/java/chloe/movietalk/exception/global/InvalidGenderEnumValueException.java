package chloe.movietalk.exception.global;

import chloe.movietalk.exception.CustomException;

public class InvalidGenderEnumValueException extends CustomException {

    public static final CustomException EXCEPTION = new InvalidGenderEnumValueException();

    private InvalidGenderEnumValueException() {
        super(GlobalErrorCode.INVALID_GENDER_ENUM_VALUE);
    }
}
