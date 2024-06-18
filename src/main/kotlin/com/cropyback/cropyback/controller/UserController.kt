package com.cropyback.cropyback.controller

import com.cropyback.cropyback.database.entities.user.UserEntity
import com.cropyback.cropyback.service.UserService
import org.bson.types.ObjectId
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api")
class UserController(
    private val userService: UserService
) {

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users")
    fun getAllUser(): ResponseEntity<List<UserEntity>> {
        val userList = userService.getAllUsers()
        val response = ResponseEntity.ok(userList)
        return response
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users/{username}")
    fun getByUsername(@Param("username") username: String): ResponseEntity<UserEntity> {
        val user = userService.findUserByUsername(username)
        return ResponseEntity.ok().body(user)
    }
}