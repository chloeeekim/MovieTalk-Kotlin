package chloe.movietalk.repository;

import chloe.movietalk.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Page<Review> findByMovieId(UUID movieId, Pageable pageable);

    Page<Review> findByUserId(UUID userId, Pageable pageable);
}
