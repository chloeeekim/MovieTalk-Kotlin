package chloe.movietalk.exception.director

import chloe.movietalk.exception.CustomException

object DirectorNotFoundException : CustomException(DirectorErrorCode.DIRECTOR_NOT_FOUND) {
    val EXCEPTION: CustomException = this
}
