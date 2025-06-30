package chloe.movietalk.auth

import chloe.movietalk.domain.SiteUser
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val user: SiteUser
) : UserDetails {

    override fun getAuthorities(): List<SimpleGrantedAuthority> {
        val roles: MutableList<String?> = ArrayList<String?>()
        roles.add("ROLE_" + user.role.toString())

        return roles.map { SimpleGrantedAuthority(it) }
    }

    override fun getPassword(): String {
        return user.passwordHash
    }

    override fun getUsername(): String? {
        return user.id.toString()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
