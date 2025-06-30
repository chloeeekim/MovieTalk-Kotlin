package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object InvalidPasswordException : CustomException(AuthErrorCode.INVALID_PASSWORD) {
    val EXCEPTION: CustomException = this
}
