package chloe.movietalk.exception.auth

import chloe.movietalk.exception.CustomException

object UserNotFoundException : CustomException(AuthErrorCode.USER_NOT_FOUND)