package chloe.movietalk.service.impl

import chloe.movietalk.domain.Director
import chloe.movietalk.domain.Movie
import chloe.movietalk.dto.request.DirectorRequest
import chloe.movietalk.dto.response.director.DirectorDetailResponse
import chloe.movietalk.dto.response.director.DirectorInfoResponse
import chloe.movietalk.exception.CustomException
import chloe.movietalk.exception.director.DirectorNotFoundException
import chloe.movietalk.exception.movie.MovieNotFoundException
import chloe.movietalk.repository.DirectorRepository
import chloe.movietalk.repository.MovieRepository
import chloe.movietalk.service.DirectorService
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

@Service
@Transactional
@RequiredArgsConstructor
class DirectorServiceImpl : DirectorService {
    private val movieRepository: MovieRepository? = null
    private val directorRepository: DirectorRepository? = null

    override fun getAllDirectors(pageable: Pageable): Page<DirectorInfoResponse?>? {
        return directorRepository!!.findAll(pageable)
            .map<DirectorInfoResponse?>(Function { obj: Director? -> DirectorInfoResponse.Companion.fromEntity() })
    }

    override fun getDirectorById(id: UUID): DirectorDetailResponse? {
        val director = directorRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { DirectorNotFoundException.EXCEPTION })
        return DirectorDetailResponse.fromEntity(director)
    }

    override fun searchDirector(keyword: String, pageable: Pageable): Page<DirectorInfoResponse?>? {
        return directorRepository!!.findByNameContaining(keyword, pageable)
            .map<DirectorInfoResponse?>(Function { obj: Director? -> DirectorInfoResponse.Companion.fromEntity() })
    }

    override fun createDirector(request: DirectorRequest): DirectorInfoResponse? {
        val save = directorRepository!!.save<Director>(request.toEntity())
        return DirectorInfoResponse.fromEntity(save)
    }

    override fun updateDirector(id: UUID, request: DirectorRequest): DirectorInfoResponse? {
        val director = directorRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { DirectorNotFoundException.EXCEPTION })

        director.updateDirector(request.toEntity())
        return DirectorInfoResponse.fromEntity(director)
    }

    override fun deleteDirector(id: UUID) {
        directorRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { DirectorNotFoundException.EXCEPTION })
        directorRepository.deleteById(id)
    }

    override fun updateDirectorFilmography(id: UUID, filmography: MutableList<UUID?>): DirectorDetailResponse? {
        val director = directorRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { DirectorNotFoundException.EXCEPTION })

        director.filmography
            .forEach(Consumer { obj: Movie? -> obj!!.removeDirector() })
        director.filmography.clear()

        filmography.stream()
            .map<Movie?> { l: UUID? ->
                movieRepository!!.findById(l)
                    .orElseThrow<CustomException?>(Supplier { MovieNotFoundException.EXCEPTION })
            }
            .forEach { m: Movie? -> m!!.changeDirector(director) }

        return DirectorDetailResponse.fromEntity(director)
    }
}
