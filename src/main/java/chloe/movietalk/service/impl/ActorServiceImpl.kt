package chloe.movietalk.service.impl

import chloe.movietalk.domain.Actor
import chloe.movietalk.dto.request.ActorRequest
import chloe.movietalk.dto.response.actor.ActorDetailResponse
import chloe.movietalk.dto.response.actor.ActorInfoResponse
import chloe.movietalk.exception.actor.ActorNotFoundException
import chloe.movietalk.exception.movie.MovieNotFoundException
import chloe.movietalk.repository.ActorRepository
import chloe.movietalk.repository.MovieRepository
import chloe.movietalk.service.ActorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ActorServiceImpl(
    private val actorRepository: ActorRepository,
    private val movieRepository: MovieRepository
) : ActorService {
    override fun getAllActors(pageable: Pageable): Page<ActorInfoResponse> {
        return actorRepository.findAll(pageable).map { ActorInfoResponse.fromEntity(it) }
    }

    override fun getActorById(id: UUID): ActorDetailResponse {
        val actor = actorRepository.findByIdOrNull(id)
            ?: throw ActorNotFoundException.EXCEPTION
        return ActorDetailResponse.fromEntity(actor)
    }

    override fun searchActor(keyword: String, pageable: Pageable): Page<ActorInfoResponse> {
        return actorRepository.findByNameContaining(keyword, pageable).map { ActorInfoResponse.fromEntity(it) }
    }

    override fun createActor(request: ActorRequest): ActorInfoResponse {
        val actor = actorRepository.save<Actor>(request.toEntity())
        return ActorInfoResponse.fromEntity(actor)
    }

    override fun updateActor(id: UUID, request: ActorRequest): ActorInfoResponse {
        val actor = actorRepository.findByIdOrNull(id)
            ?: throw ActorNotFoundException.EXCEPTION

        actor.updateActor(request.toEntity())
        return ActorInfoResponse.fromEntity(actor)
    }

    override fun deleteActor(id: UUID) {
        actorRepository.findByIdOrNull(id)
            ?: throw ActorNotFoundException.EXCEPTION
        actorRepository.deleteById(id)
    }

    override fun updateActorFilmography(id: UUID, filmography: List<UUID>): ActorDetailResponse {
        val actor = actorRepository.findByIdOrNull(id)
            ?: throw ActorNotFoundException.EXCEPTION

        actor.movieActors.clear()

        filmography
            .map { movieRepository.findByIdOrNull(it) ?: throw MovieNotFoundException.EXCEPTION }
            .forEach { actor.addMovie(it) }

        return ActorDetailResponse.fromEntity(actor)
    }
}
