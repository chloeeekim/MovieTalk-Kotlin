package chloe.movietalk.exception.movie

import chloe.movietalk.exception.CustomException

object MovieNotFoundException : CustomException(MovieErrorCode.MOVIE_NOT_FOUND) {
    val EXCEPTION: CustomException = this
}
