package chloe.movietalk.controller.impl;

import chloe.movietalk.controller.DirectorController;
import chloe.movietalk.dto.request.DirectorRequest;
import chloe.movietalk.dto.response.director.DirectorDetailResponse;
import chloe.movietalk.dto.response.director.DirectorInfoResponse;
import chloe.movietalk.service.DirectorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directors")
@Tag(name = "Director", description = "Director APIs - 감독 목록 조회, 생성, 수정, 삭제 및 필모그라피 갱신 기능 제공")
public class DirectorControllerImpl implements DirectorController {

    private final DirectorService directorService;

    public ResponseEntity<Page<DirectorInfoResponse>> getAllDirectors(Pageable pageable) {
        Page<DirectorInfoResponse> directors = directorService.getAllDirectors(pageable);
        return ResponseEntity.ok().body(directors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorDetailResponse> getDirectorById(UUID id) {
        DirectorDetailResponse director = directorService.getDirectorById(id);
        return ResponseEntity.ok().body(director);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DirectorInfoResponse>> searchDirectors(String keyword, Pageable pageable) {
        Page<DirectorInfoResponse> directors = directorService.searchDirector(keyword, pageable);
        return ResponseEntity.ok().body(directors);
    }

    @PostMapping
    public ResponseEntity<DirectorInfoResponse> createDirector(DirectorRequest request) {
        DirectorInfoResponse director = directorService.createDirector(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(director);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DirectorInfoResponse> updateDirector(UUID id, DirectorRequest request) {
        DirectorInfoResponse director = directorService.updateDirector(id, request);
        return ResponseEntity.ok().body(director);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirector(UUID id) {
        directorService.deleteDirector(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/filmography")
    public ResponseEntity<DirectorDetailResponse> updateDirectorFilmography(UUID id, List<UUID> filmography) {
        DirectorDetailResponse director = directorService.updateDirectorFilmography(id, filmography);
        return ResponseEntity.ok().body(director);
    }
}
