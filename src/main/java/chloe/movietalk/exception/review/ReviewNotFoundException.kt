package chloe.movietalk.exception.review

import chloe.movietalk.exception.CustomException

object ReviewNotFoundException : CustomException(ReviewErrorCode.REVIEW_NOT_FOUND) {
    val EXCEPTION: CustomException = this
}
