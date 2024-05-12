package com.cropyback.cropyback.service

import com.cropyback.cropyback.database.entities.user.ImageEntity
import com.cropyback.cropyback.repository.ImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService {

    @Autowired
    lateinit var imageRepository: ImageRepository

    fun saveImage(image: ImageEntity) {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            imageRepository.save(image)
        }
    }

    fun getImage(id: ObjectId): ImageEntity {
        return imageRepository.findById(id).get()
    }

}