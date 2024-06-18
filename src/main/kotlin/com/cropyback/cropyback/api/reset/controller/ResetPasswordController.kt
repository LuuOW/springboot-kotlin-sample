package com.cropyback.cropyback.api.reset.controller

import ResetPasswordRequest
import com.cropyback.cropyback.api.reset.PasswordResetService
import com.cropyback.cropyback.database.entities.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/api")
class ResetPasswordController(
    private val mongoTemplate: MongoTemplate
    ) {

    @Autowired
    private lateinit var passwordResetService: PasswordResetService

    @PostMapping("/password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest) {
        val phone = request.phone
        val password = request.newPassword
        val query = Query().addCriteria(Criteria.where("phone").`is`(phone))

        if (mongoTemplate.exists(query, UserEntity::class.java)) {
            passwordResetService.resetUserPassword(phone, password)
        }

    }

    @GetMapping("/password")
    fun getWelcomeTo(): ResponseEntity<String> {
        return ResponseEntity.ok("Whale hello there")
    }

}