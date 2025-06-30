package chloe.movietalk.exception.global

import chloe.movietalk.exception.CustomException

object InvalidEnumValueException : CustomException(GlobalErrorCode.INVALID_ENUM_VALUE) {
    val EXCEPTION: CustomException = this
}
