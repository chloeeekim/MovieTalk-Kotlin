package chloe.movietalk.auth

import chloe.movietalk.domain.SiteUser
import lombok.Getter
import lombok.RequiredArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Getter
@RequiredArgsConstructor
class CustomUserDetails : UserDetails {
    private val user: SiteUser? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority?> {
        val roles: MutableList<String?> = ArrayList<String?>()
        roles.add("ROLE_" + user!!.role.toString())

        return roles.stream()
            .map<SimpleGrantedAuthority?> { role: String? -> SimpleGrantedAuthority(role) }
            .toList()
    }

    override fun getPassword(): String {
        return user!!.passwordHash
    }

    override fun getUsername(): String? {
        return user!!.id.toString()
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
