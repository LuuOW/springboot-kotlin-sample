package com.cropyback.cropyback.controller

import com.cropyback.cropyback.database.sql.service.UserJpaService
import com.cropyback.cropyback.database.sql.user.UserModel
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api")
class UserController(
    private val userService: UserJpaService
) {

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users")
    fun getAllUser(): ResponseEntity<MutableList<UserModel>> {
        val userList = userService.getAllUsers()
        val response = ResponseEntity.ok(userList)
        return response
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    fun getByUsername(@Param("username") username: String): ResponseEntity<UserModel> {
        val user = userService.findUserByUsername(username)
        return ResponseEntity.ok().body(user)
    }
}