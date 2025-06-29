package chloe.movietalk.exception.review;

import chloe.movietalk.exception.CustomException;

public class ReviewlikeNotFoundException extends CustomException {

    public static final CustomException EXCEPTION = new ReviewlikeNotFoundException();

    private ReviewlikeNotFoundException() {
        super(ReviewErrorCode.REVIEWLIKE_NOT_FOUND);
    }
}
