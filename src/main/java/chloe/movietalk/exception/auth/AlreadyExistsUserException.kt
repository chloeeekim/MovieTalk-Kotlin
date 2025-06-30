package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object AlreadyExistsUserException : CustomException(AuthErrorCode.ALREADY_EXISTS_USER) {
    val EXCEPTION: CustomException = this
}
