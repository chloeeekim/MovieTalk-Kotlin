package chloe.movietalk.service

import chloe.movietalk.dto.request.LoginRequest
import chloe.movietalk.dto.request.SignupRequest
import chloe.movietalk.dto.response.user.UserInfoResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface UserService {
    fun signUp(request: SignupRequest): UserInfoResponse

    fun logIn(loginRequest: LoginRequest, request: HttpServletRequest, response: HttpServletResponse): UserInfoResponse

    fun refresh(request: HttpServletRequest, response: HttpServletResponse)

    fun logout(request: HttpServletRequest)
}
