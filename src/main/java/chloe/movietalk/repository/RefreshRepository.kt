package chloe.movietalk.repository

import chloe.movietalk.domain.Refresh
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RefreshRepository : CrudRepository<Refresh?, UUID?> {
    fun findByUserId(id: UUID?): Optional<Refresh?>?
}
