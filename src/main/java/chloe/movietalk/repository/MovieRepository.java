package chloe.movietalk.repository;

import chloe.movietalk.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {
    Page<Movie> findByTitleContaining(String keyword, Pageable pageable);

    Optional<Movie> findByCodeFIMS(String code);

    Page<Movie> findByDirectorId(UUID directorId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"reviews"})
    Optional<Movie> findById(UUID id);

    Page<Movie> findAll(Pageable pageable);
}
