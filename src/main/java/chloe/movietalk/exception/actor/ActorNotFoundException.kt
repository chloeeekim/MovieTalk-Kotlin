package chloe.movietalk.exception.actor;

import chloe.movietalk.exception.CustomException;

public class ActorNotFoundException extends CustomException {
    public static final CustomException EXCEPTION = new ActorNotFoundException();

    private ActorNotFoundException() {
        super(ActorErrorCode.ACTOR_NOT_FOUND);
    }
}
