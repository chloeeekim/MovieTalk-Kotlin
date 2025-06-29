package chloe.movietalk.repository;

import chloe.movietalk.domain.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DirectorRepository extends JpaRepository<Director, UUID> {
    Page<Director> findByNameContaining(String keyword, Pageable pageable);

    Page<Director> findAll(Pageable pageable);
}
