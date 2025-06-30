package chloe.movietalk.service

import chloe.movietalk.dto.request.DirectorRequest
import chloe.movietalk.dto.response.director.DirectorDetailResponse
import chloe.movietalk.dto.response.director.DirectorInfoResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface DirectorService {
    fun getAllDirectors(pageable: Pageable): Page<DirectorInfoResponse>

    fun getDirectorById(id: UUID): DirectorDetailResponse

    fun searchDirector(keyword: String, pageable: Pageable): Page<DirectorInfoResponse>

    fun createDirector(request: DirectorRequest): DirectorInfoResponse

    fun updateDirector(id: UUID, request: DirectorRequest): DirectorInfoResponse

    fun deleteDirector(id: UUID)

    fun updateDirectorFilmography(id: UUID, movieIds: List<UUID>): DirectorDetailResponse
}
