package chloe.movietalk.repository

import chloe.movietalk.domain.SiteUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<SiteUser, UUID> {
    fun findByEmail(email: String): SiteUser?
}
