package chloe.movietalk.config

import chloe.movietalk.auth.*
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {
    private val customUserDetailService: CustomUserDetailService? = null
    private val jwtProvider: JwtProvider? = null
    private val accessDeniedHandler: CustomAccessDeniedHandler? = null
    private val authenticationEntryPointHandler: CustomAuthenticationEntryPointHandler? = null
    private val authFilter: JwtAuthFilter? = null

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http.csrf(Customizer { obj: CsrfConfigurer<HttpSecurity?>? -> obj!!.disable() })
        http.cors((Customizer.withDefaults<CorsConfigurer<HttpSecurity?>?>()))

        // 세션 관리 상태 없음 (Spring Security가 세션 생성 또는 사용하지 않음)
        http.sessionManagement(Customizer { session: SessionManagementConfigurer<HttpSecurity?>? ->
            session!!.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        })

        // FormLogin, BasicHttp 비활성화
        http.formLogin(Customizer { obj: FormLoginConfigurer<HttpSecurity?>? -> obj!!.disable() })
        http.httpBasic(Customizer { obj: HttpBasicConfigurer<HttpSecurity?>? -> obj!!.disable() })

        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(
            authFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )

        // exception handling
        http.exceptionHandling(Customizer { conf: ExceptionHandlingConfigurer<HttpSecurity?>? ->
            conf!!
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(accessDeniedHandler)
        })

        //        // http request 인증 설정
//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers(AUTH_WHITELIST).permitAll()
//                // admin 요청은 관리자 권한만 가능
//                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
//        );

        // 테스트를 위해 우선 모든 request를 허용
        http.authorizeHttpRequests(Customizer { authorize: AuthorizationManagerRequestMatcherRegistry? ->
            authorize
                .anyRequest().permitAll()
        })

        return http.build()
    }

    companion object {
        private val AUTH_WHITELIST = arrayOf<String?>(
            "/api/login", "/api/signup",
            "/swagger-ui/**", "/api-docs", "api-docs/**", "/swagger-ui.html"
        )
    }
}
