package chloe.movietalk.service.impl

import chloe.movietalk.dto.request.DirectorRequest
import chloe.movietalk.dto.response.director.DirectorDetailResponse
import chloe.movietalk.dto.response.director.DirectorInfoResponse
import chloe.movietalk.exception.director.DirectorNotFoundException
import chloe.movietalk.exception.movie.MovieNotFoundException
import chloe.movietalk.repository.DirectorRepository
import chloe.movietalk.repository.MovieRepository
import chloe.movietalk.service.DirectorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class DirectorServiceImpl(
    private val movieRepository: MovieRepository,
    private val directorRepository: DirectorRepository
) : DirectorService {
    override fun getAllDirectors(pageable: Pageable): Page<DirectorInfoResponse> {
        return directorRepository.findAll(pageable).map { DirectorInfoResponse.fromEntity(it) }
    }

    override fun getDirectorById(id: UUID): DirectorDetailResponse {
        val director = directorRepository.findByIdOrNull(id)
            ?: throw DirectorNotFoundException.EXCEPTION
        return DirectorDetailResponse.fromEntity(director)
    }

    override fun searchDirector(keyword: String, pageable: Pageable): Page<DirectorInfoResponse> {
        return directorRepository.findByNameContaining(keyword, pageable).map { DirectorInfoResponse.fromEntity(it) }
    }

    override fun createDirector(request: DirectorRequest): DirectorInfoResponse {
        val save = directorRepository.save(request.toEntity())
        return DirectorInfoResponse.fromEntity(save)
    }

    override fun updateDirector(id: UUID, request: DirectorRequest): DirectorInfoResponse {
        val director = directorRepository.findByIdOrNull(id)
            ?: throw DirectorNotFoundException.EXCEPTION

        director.updateDirector(request.toEntity())
        return DirectorInfoResponse.fromEntity(director)
    }

    override fun deleteDirector(id: UUID) {
        directorRepository.findByIdOrNull(id)
            ?: throw DirectorNotFoundException.EXCEPTION
        directorRepository.deleteById(id)
    }

    override fun updateDirectorFilmography(id: UUID, filmography: List<UUID>): DirectorDetailResponse {
        val director = directorRepository.findByIdOrNull(id)
            ?: throw DirectorNotFoundException.EXCEPTION

        director.filmography.forEach { it.removeDirector() }
        director.filmography.clear()

        filmography
            .map { movieRepository.findByIdOrNull(it) ?: throw MovieNotFoundException.EXCEPTION }
            .forEach { it.changeDirector(director) }

        return DirectorDetailResponse.fromEntity(director)
    }
}
