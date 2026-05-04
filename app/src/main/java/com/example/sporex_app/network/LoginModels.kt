package com.example.sporex_app.network

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserDto(
    val id: String?,
    val email: String?,
    val name: String?
)


