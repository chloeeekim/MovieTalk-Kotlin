package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object InvalidRefreshToken : CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN)