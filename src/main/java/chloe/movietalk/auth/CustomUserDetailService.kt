package chloe.movietalk.auth

import chloe.movietalk.exception.auth.UserNotFoundException
import chloe.movietalk.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByIdOrNull(UUID.fromString(username))
            ?: throw UserNotFoundException.EXCEPTION
        return CustomUserDetails(user)
    }
}
