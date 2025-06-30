package chloe.movietalk.exception

interface BaseErrorCode {
    @JvmField
    val errorReason: ErrorReason?
}
