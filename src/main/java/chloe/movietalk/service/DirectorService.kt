package chloe.movietalk.service;

import chloe.movietalk.dto.request.DirectorRequest;
import chloe.movietalk.dto.response.director.DirectorDetailResponse;
import chloe.movietalk.dto.response.director.DirectorInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface DirectorService {

    public Page<DirectorInfoResponse> getAllDirectors(Pageable pageable);

    public DirectorDetailResponse getDirectorById(UUID id);

    public Page<DirectorInfoResponse> searchDirector(String keyword, Pageable pageable);

    public DirectorInfoResponse createDirector(DirectorRequest request);

    public DirectorInfoResponse updateDirector(UUID id, DirectorRequest request);

    public void deleteDirector(UUID id);

    public DirectorDetailResponse updateDirectorFilmography(UUID id, List<UUID> movieIds);
}
