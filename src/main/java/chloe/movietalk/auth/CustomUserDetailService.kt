package chloe.movietalk.auth;

import chloe.movietalk.domain.SiteUser;
import chloe.movietalk.exception.auth.UserNotFoundException;
import chloe.movietalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SiteUser user = userRepository.findById(UUID.fromString(username))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        return new CustomUserDetails(user);
    }
}
