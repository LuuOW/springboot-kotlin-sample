package com.cropyback.cropyback.api.reset

import com.cropyback.cropyback.database.entities.user.UserEntity
import com.cropyback.cropyback.repository.UserRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordResetService(
    private val mongoTemplate: MongoTemplate,
    private val usersRepository: UserRepository
){

    fun resetUserPassword(email: String, newPassword: String) {
        val query = Query().addCriteria(Criteria.where("email").`is`(email))

        mongoTemplate.findOne(query, UserEntity::class.java).let { user ->
            val bcrypt = BCryptPasswordEncoder()
            val hashedPassword = bcrypt.encode(newPassword)
            val updatedUser =  user?.copy(password = hashedPassword)
            usersRepository.save(updatedUser!!)
        }
    }

}