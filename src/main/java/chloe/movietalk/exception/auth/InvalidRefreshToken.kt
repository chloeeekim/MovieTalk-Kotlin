package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object InvalidRefreshToken : CustomException() {
    val EXCEPTION: CustomException = InvalidRefreshToken()
}
