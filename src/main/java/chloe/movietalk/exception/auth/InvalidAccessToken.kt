package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object InvalidAccessToken : CustomException() {
    val EXCEPTION: CustomException = InvalidAccessToken()
}
