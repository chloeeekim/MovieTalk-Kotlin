package chloe.movietalk.exception.review

import chloe.movietalk.exception.CustomException

object AlreadyLikedReviewException : CustomException(ReviewErrorCode.ALREADY_LIKED_REVIEW) {
    val EXCEPTION: CustomException = this
}
