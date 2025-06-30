package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object UserNotFoundException : CustomException() {
    val EXCEPTION: CustomException = UserNotFoundException()
}
