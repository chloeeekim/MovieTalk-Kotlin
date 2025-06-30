package chloe.movietalk.config

import chloe.movietalk.auth.CustomAccessDeniedHandler
import chloe.movietalk.auth.CustomAuthenticationEntryPointHandler
import chloe.movietalk.auth.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val accessDeniedHandler: CustomAccessDeniedHandler,
    private val authenticationEntryPointHandler: CustomAuthenticationEntryPointHandler,
    private val authFilter: JwtAuthFilter
) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors {  } // 기본 설정
            .sessionManagement {
                // 세션 관리 상태 없음 (Spring Security가 세션 생성 또는 사용하지 않음)
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // FormLogin, BasicHttp 비활성화
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it
                    .authenticationEntryPoint(authenticationEntryPointHandler)
                    .accessDeniedHandler(accessDeniedHandler)
            }
            .authorizeHttpRequests {
                it
//                    .requestMatchers(*AUTH_WHITELIST).permitAll()
//                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                    .anyRequest().authenticated()
                    .anyRequest().permitAll() // 테스트를 위해 우선 모든 request 허용
            }

        return http.build()
    }

    companion object {
        private val AUTH_WHITELIST = arrayOf(
            "/api/login", "/api/signup",
            "/swagger-ui/**", "/api-docs", "api-docs/**", "/swagger-ui.html"
        )
    }
}
