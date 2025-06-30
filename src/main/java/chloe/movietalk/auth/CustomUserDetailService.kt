package chloe.movietalk.auth

import chloe.movietalk.exception.CustomException
import chloe.movietalk.exception.auth.UserNotFoundException
import chloe.movietalk.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Supplier

@Service
@RequiredArgsConstructor
class CustomUserDetailService : UserDetailsService {
    private val userRepository: UserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository!!.findById(UUID.fromString(username))
            .orElseThrow<CustomException?>(Supplier { UserNotFoundException.EXCEPTION })
        return CustomUserDetails(user)
    }
}
