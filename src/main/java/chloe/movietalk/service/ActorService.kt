package chloe.movietalk.service

import chloe.movietalk.dto.request.ActorRequest
import chloe.movietalk.dto.response.actor.ActorDetailResponse
import chloe.movietalk.dto.response.actor.ActorInfoResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ActorService {
    fun getAllActors(pageable: Pageable): Page<ActorInfoResponse>

    fun getActorById(id: UUID): ActorDetailResponse

    fun searchActor(keyword: String, pageable: Pageable): Page<ActorInfoResponse>

    fun createActor(request: ActorRequest): ActorInfoResponse

    fun updateActor(id: UUID, request: ActorRequest): ActorInfoResponse

    fun deleteActor(id: UUID)

    fun updateActorFilmography(id: UUID, filmography: List<UUID>): ActorDetailResponse
}
