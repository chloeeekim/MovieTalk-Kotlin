package chloe.movietalk.exception.movie;

import chloe.movietalk.exception.CustomException;

public class AlreadyExistsMovieException extends CustomException {

    public static final CustomException EXCEPTION = new AlreadyExistsMovieException();

    private AlreadyExistsMovieException() {
        super(MovieErrorCode.ALREADY_EXISTS_MOVIE);
    }
}