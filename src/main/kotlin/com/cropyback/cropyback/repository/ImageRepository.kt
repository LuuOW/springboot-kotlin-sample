package com.cropyback.cropyback.repository

import com.cropyback.cropyback.database.entities.user.ImageEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ImageRepository: MongoRepository<ImageEntity, ObjectId> {

}