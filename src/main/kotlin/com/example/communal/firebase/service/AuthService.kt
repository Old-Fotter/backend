package com.example.communal.firebase.service

import com.example.communal.firebase.dto.RegisterRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import org.springframework.stereotype.Service

@Service
class AuthService {

    fun registerUser(request: RegisterRequest): String {
        val user = UserRecord.CreateRequest()
            .setEmail(request.email)
            .setPassword(request.password)
            .setEmailVerified(false)

        val createdUser = FirebaseAuth.getInstance().createUser(user)

        val emailVerificationLink = FirebaseAuth.getInstance()
            .generateEmailVerificationLink(request.email)

        return "Перейдите по ссылке для подтверждения: $emailVerificationLink"
    }
}

