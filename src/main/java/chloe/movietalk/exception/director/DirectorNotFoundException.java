package chloe.movietalk.exception.director;

import chloe.movietalk.exception.CustomException;

public class DirectorNotFoundException extends CustomException {
    public static final CustomException EXCEPTION = new DirectorNotFoundException();

    private DirectorNotFoundException() {
        super(DirectorErrorCode.DIRECTOR_NOT_FOUND);
    }
}
