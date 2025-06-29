package chloe.movietalk.exception.review;

import chloe.movietalk.exception.CustomException;

public class AlreadyLikedReviewException extends CustomException {

    public static final CustomException EXCEPTION = new AlreadyLikedReviewException();

    private AlreadyLikedReviewException() {
        super(ReviewErrorCode.ALREADY_LIKED_REVIEW);
    }
}
