package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object InvalidAccessToken : CustomException(AuthErrorCode.INVALID_ACCESS_TOKEN)