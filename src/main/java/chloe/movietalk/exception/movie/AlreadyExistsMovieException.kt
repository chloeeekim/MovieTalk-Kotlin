package chloe.movietalk.exception.movie

import chloe.movietalk.exception.CustomException

object AlreadyExistsMovieException : CustomException(MovieErrorCode.ALREADY_EXISTS_MOVIE) {
    val EXCEPTION: CustomException = this
}