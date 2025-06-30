package chloe.movietalk.exception.actor

import chloe.movietalk.exception.CustomException

object ActorNotFoundException : CustomException(ActorErrorCode.ACTOR_NOT_FOUND) {
    val EXCEPTION: CustomException = this
}
