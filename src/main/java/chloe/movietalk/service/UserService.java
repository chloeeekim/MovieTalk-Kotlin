package chloe.movietalk.service;

import chloe.movietalk.dto.request.LoginRequest;
import chloe.movietalk.dto.request.SignupRequest;
import chloe.movietalk.dto.response.user.UserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    public UserInfoResponse signUp(SignupRequest request);

    public UserInfoResponse logIn(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);

    public void refresh(HttpServletRequest request, HttpServletResponse response);

    public void logout(HttpServletRequest request);
}
