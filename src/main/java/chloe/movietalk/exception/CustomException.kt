package chloe.movietalk.exception

open class CustomException(
    val errorCode: BaseErrorCode
) : RuntimeException()
