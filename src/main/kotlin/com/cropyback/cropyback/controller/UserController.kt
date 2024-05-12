package com.cropyback.cropyback.controller

import com.cropyback.cropyback.database.entities.user.UserEntity
import com.cropyback.cropyback.service.UserService
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/users")
    fun getAllUser(): ResponseEntity<List<UserEntity>> {
        val userList = userService.getAllUsers()
        val response = ResponseEntity.ok(userList)
        return response
    }

    @GetMapping("/users/{username}")
    fun getByUsername(@Param("username") username: String): ResponseEntity<String> {
        val user = userService.loadUserByUsername(username)
        val response = ResponseEntity.ok(user.username)
        return response
    }

}