package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object AlreadyExistsUserException : CustomException() {
    val EXCEPTION: CustomException = AlreadyExistsUserException()
}
