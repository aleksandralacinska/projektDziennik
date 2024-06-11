package com.example.dziennik.repository

import com.example.dziennik.model.User

class oldUserRepository {
    fun getUser(): User {
        // Zwróć przykładowe dane
        return User(1, "John Doe", "john.doe@example.com")
    }
}
