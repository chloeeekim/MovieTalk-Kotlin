package chloe.movietalk.exception.movie;

import chloe.movietalk.exception.CustomException;

public class MovieNotFoundException extends CustomException {

    public static final CustomException EXCEPTION = new MovieNotFoundException();

    private MovieNotFoundException() {
        super(MovieErrorCode.MOVIE_NOT_FOUND);
    }
}
