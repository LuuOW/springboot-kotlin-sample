package com.cropyback.cropyback.database.sql.user

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
@Table(name = "users")
data class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val username: String = "",
    val email: String = "",
    @Column(nullable = false, unique = true)
    val phone: String = "",
    val password: String = "",
    val role: String = "USER",
    val created_at: String = formatLocalDateTimeToString(LocalDateTime.now()),
    val updated_at: String = "",
    val images: MutableList<String> = mutableListOf(),
    val chats: MutableList<String> = mutableListOf()
)

fun formatLocalDateTimeToString(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return dateTime.format(formatter)
}
