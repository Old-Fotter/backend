package com.example.communal.firebase.controller

import com.example.communal.firebase.dto.RegisterRequest
import com.example.communal.firebase.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): String {
        val verificationLink = authService.registerUser(request)
        return "Перейдите по ссылке для подтверждения: $verificationLink"
    }
}