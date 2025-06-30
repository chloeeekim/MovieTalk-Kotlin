package chloe.movietalk.controller.impl

import chloe.movietalk.controller.UserController
import chloe.movietalk.dto.request.LoginRequest
import chloe.movietalk.dto.request.SignupRequest
import chloe.movietalk.dto.response.user.UserInfoResponse
import chloe.movietalk.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "User", description = "User APIs - 사용자 회원가입, 로그인 기능 제공")
class UserControllerImpl : UserController {
    private val userService: UserService? = null

    @PostMapping("/signup")
    override fun signup(request: SignupRequest): ResponseEntity<UserInfoResponse?> {
        val response = userService!!.signUp(request)
        return ResponseEntity.status(HttpStatus.CREATED).body<UserInfoResponse?>(response)
    }

    @PostMapping("/login")
    override fun login(
        loginRequest: LoginRequest,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<UserInfoResponse?> {
        val loginResponse = userService!!.logIn(loginRequest, request, response)
        return ResponseEntity.ok().body<UserInfoResponse?>(loginResponse)
    }

    @PostMapping("/refresh")
    override fun refresh(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Void?> {
        userService!!.refresh(request, response)
        return ResponseEntity.ok().build<Void?>()
    }

    @PostMapping("/logout")
    override fun logout(request: HttpServletRequest): ResponseEntity<Void?> {
        userService!!.logout(request)
        return ResponseEntity.ok().build<Void?>()
    }
}
