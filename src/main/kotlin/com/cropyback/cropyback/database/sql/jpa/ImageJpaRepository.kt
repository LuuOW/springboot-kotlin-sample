package com.cropyback.cropyback.database.sql.jpa

import com.cropyback.cropyback.database.sql.user.ImageModel
import org.springframework.data.jpa.repository.JpaRepository

interface ImageJpaRepository: JpaRepository<ImageModel, Long> {

    fun findByName(name: String): ImageModel?

}