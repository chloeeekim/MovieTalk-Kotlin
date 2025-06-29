package chloe.movietalk.repository;

import chloe.movietalk.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    boolean existsByUserIdAndReviewId(UUID userId, UUID reviewId);

    Optional<ReviewLike> findByUserIdAndReviewId(UUID userId, UUID reviewId);
}
