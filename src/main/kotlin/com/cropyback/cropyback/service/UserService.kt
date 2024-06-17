package com.cropyback.cropyback.service

import com.cropyback.cropyback.database.entities.user.UserEntity
import com.cropyback.cropyback.database.entities.auth.SignUpRequest
import com.cropyback.cropyback.repository.UserRepository
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val mongoTemplate: MongoTemplate
) : UserDetailsService {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun areCredentialsUnique(
        username: String,
        email: String,
        phone: String
    ): Boolean {

        val criteria = Criteria().orOperator(
            Criteria.where("username").`is`(username),
            Criteria.where("email").`is`(email),
            Criteria.where("phone").`is`(phone)
        )
        val query = Query(criteria)

        return mongoTemplate.exists(query, UserEntity::class.java)
    }

    fun emailExist(email:String): Boolean {
        val criteria = Criteria().orOperator(Criteria.where("email").`is`(email))
        val query = Query(criteria)
        return mongoTemplate.exists(query, UserEntity::class.java)
    }

    fun phoneNumberExist(phone: String): Boolean {
        val criteria = Criteria().orOperator(Criteria.where("phone").`is`(phone))
        val query = Query(criteria)
        return mongoTemplate.exists(query, UserEntity::class.java)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = getAllUsers().find { it.username == username }
        val userDetails = User.builder()
            .username(user?.username)
            .password(user?.password)
            .roles(user?.role)
            .build()
        return userDetails
    }

    fun updateUserImages(user: UserEntity) {
        userRepository.save(user)
    }

    fun findUserById(id: String): UserEntity {
        return userRepository.findById(id).get()
    }


    fun saveUser(signUpReq: SignUpRequest) {
        val newUser = signUpReq.toUser(passwordEncoder)
        userRepository.save(
            UserEntity(
                username = newUser.username,
                email = newUser.email,
                phone = newUser.phone,
                password = newUser.password,
                role = newUser.role
            )
        )
    }

    fun updateUser(user: UserEntity) {
        userRepository.save(user)
    }

    fun getAllUsers(): List<UserEntity> {
        return userRepository.findAll()
    }


}