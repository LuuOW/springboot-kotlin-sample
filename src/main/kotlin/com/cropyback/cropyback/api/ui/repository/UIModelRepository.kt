package com.cropyback.cropyback.api.ui.repository
import org.springframework.data.mongodb.repository.MongoRepository
import org.bson.types.ObjectId
import com.cropyback.cropyback.api.ui.models.UIModel

interface UIModelRepository: MongoRepository<UIModel, ObjectId> {}