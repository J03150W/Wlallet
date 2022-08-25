package com.example.wlallet

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    @Query("SELECT '*' from User where User.password = ?1")
    fun findByLogin(password: String): User?
}