package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object LoginRequiredException : CustomException(AuthErrorCode.LOGIN_REQUIRED)