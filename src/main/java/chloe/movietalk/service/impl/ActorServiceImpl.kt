package chloe.movietalk.service.impl

import chloe.movietalk.domain.Actor
import chloe.movietalk.domain.Movie
import chloe.movietalk.dto.request.ActorRequest
import chloe.movietalk.dto.response.actor.ActorDetailResponse
import chloe.movietalk.dto.response.actor.ActorInfoResponse
import chloe.movietalk.exception.CustomException
import chloe.movietalk.exception.actor.ActorNotFoundException
import chloe.movietalk.exception.movie.MovieNotFoundException
import chloe.movietalk.repository.ActorRepository
import chloe.movietalk.repository.MovieRepository
import chloe.movietalk.service.ActorService
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Function
import java.util.function.Supplier

@Service
@Transactional
@RequiredArgsConstructor
class ActorServiceImpl : ActorService {
    private val actorRepository: ActorRepository? = null
    private val movieRepository: MovieRepository? = null

    override fun getAllActors(pageable: Pageable): Page<ActorInfoResponse?>? {
        return actorRepository!!.findAll(pageable)
            .map<ActorInfoResponse?>(Function { obj: Actor? -> ActorInfoResponse.Companion.fromEntity() })
    }

    override fun getActorById(id: UUID): ActorDetailResponse? {
        val actor = actorRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { ActorNotFoundException.EXCEPTION })
        return ActorDetailResponse.fromEntity(actor)
    }

    override fun searchActor(keyword: String, pageable: Pageable): Page<ActorInfoResponse?>? {
        return actorRepository!!.findByNameContaining(keyword, pageable)
            .map<ActorInfoResponse?>(Function { obj: Actor? -> ActorInfoResponse.Companion.fromEntity() })
    }

    override fun createActor(request: ActorRequest): ActorInfoResponse? {
        val actor = actorRepository!!.save<Actor>(request.toEntity())
        return ActorInfoResponse.fromEntity(actor)
    }

    override fun updateActor(id: UUID, request: ActorRequest): ActorInfoResponse? {
        val actor = actorRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { ActorNotFoundException.EXCEPTION })

        actor.updateActor(request.toEntity())
        return ActorInfoResponse.fromEntity(actor)
    }

    override fun deleteActor(id: UUID) {
        actorRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { ActorNotFoundException.EXCEPTION })
        actorRepository.deleteById(id)
    }

    override fun updateActorFilmography(id: UUID, filmography: MutableList<UUID?>): ActorDetailResponse? {
        val actor = actorRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { ActorNotFoundException.EXCEPTION })

        actor.movieActors.clear()

        filmography.stream()
            .map<Movie?> { l: UUID? ->
                movieRepository!!.findById(l)
                    .orElseThrow<CustomException?>(Supplier { MovieNotFoundException.EXCEPTION })
            }
            .forEach { movie: Movie? -> actor.addMovie(movie!!) }

        return ActorDetailResponse.fromEntity(actor)
    }
}
