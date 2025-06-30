package chloe.movietalk.repository

import chloe.movietalk.domain.Director
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DirectorRepository : JpaRepository<Director?, UUID?> {
    fun findByNameContaining(keyword: String?, pageable: Pageable?): Page<Director?>?

    override fun findAll(pageable: Pageable?): Page<Director?>
}
