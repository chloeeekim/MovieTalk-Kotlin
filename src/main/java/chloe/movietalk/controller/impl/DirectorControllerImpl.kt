package chloe.movietalk.controller.impl

import chloe.movietalk.controller.DirectorController
import chloe.movietalk.dto.request.DirectorRequest
import chloe.movietalk.dto.response.director.DirectorDetailResponse
import chloe.movietalk.dto.response.director.DirectorInfoResponse
import chloe.movietalk.service.DirectorService
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directors")
@Tag(name = "Director", description = "Director APIs - 감독 목록 조회, 생성, 수정, 삭제 및 필모그라피 갱신 기능 제공")
class DirectorControllerImpl : DirectorController {
    private val directorService: DirectorService? = null

    override fun getAllDirectors(pageable: Pageable): ResponseEntity<Page<DirectorInfoResponse?>?> {
        val directors: Page<DirectorInfoResponse?> = directorService!!.getAllDirectors(pageable)
        return ResponseEntity.ok().body<Page<DirectorInfoResponse?>?>(directors)
    }

    @GetMapping("/{id}")
    override fun getDirectorById(id: UUID): ResponseEntity<DirectorDetailResponse?> {
        val director = directorService!!.getDirectorById(id)
        return ResponseEntity.ok().body<DirectorDetailResponse?>(director)
    }

    @GetMapping("/search")
    override fun searchDirectors(keyword: String, pageable: Pageable): ResponseEntity<Page<DirectorInfoResponse?>?> {
        val directors: Page<DirectorInfoResponse?> = directorService!!.searchDirector(keyword, pageable)
        return ResponseEntity.ok().body<Page<DirectorInfoResponse?>?>(directors)
    }

    @PostMapping
    override fun createDirector(request: DirectorRequest): ResponseEntity<DirectorInfoResponse?> {
        val director = directorService!!.createDirector(request)
        return ResponseEntity.status(HttpStatus.CREATED).body<DirectorInfoResponse?>(director)
    }

    @PutMapping("/{id}")
    override fun updateDirector(id: UUID, request: DirectorRequest): ResponseEntity<DirectorInfoResponse?> {
        val director = directorService!!.updateDirector(id, request)
        return ResponseEntity.ok().body<DirectorInfoResponse?>(director)
    }

    @DeleteMapping("/{id}")
    override fun deleteDirector(id: UUID): ResponseEntity<Void?> {
        directorService!!.deleteDirector(id)
        return ResponseEntity.noContent().build<Void?>()
    }

    @PostMapping("/{id}/filmography")
    override fun updateDirectorFilmography(
        id: UUID,
        filmography: MutableList<UUID?>
    ): ResponseEntity<DirectorDetailResponse?> {
        val director = directorService!!.updateDirectorFilmography(id, filmography)
        return ResponseEntity.ok().body<DirectorDetailResponse?>(director)
    }
}
