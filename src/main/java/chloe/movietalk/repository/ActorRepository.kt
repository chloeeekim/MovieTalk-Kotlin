package chloe.movietalk.repository

import chloe.movietalk.domain.Actor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ActorRepository : JpaRepository<Actor, UUID> {
    fun findByNameContaining(keyword: String, pageable: Pageable): Page<Actor>

    override fun findAll(pageable: Pageable): Page<Actor>
}
