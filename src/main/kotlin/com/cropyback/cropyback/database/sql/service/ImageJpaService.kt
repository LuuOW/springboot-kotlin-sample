package com.cropyback.cropyback.database.sql.service

import com.cropyback.cropyback.database.sql.jpa.ImageJpaRepository
import com.cropyback.cropyback.database.sql.user.ImageModel
import org.springframework.stereotype.Service
import java.awt.Image

@Service
class ImageJpaService(
    private val imageJpaRepository: ImageJpaRepository
) {


    fun storeBlob(name: String, data: ByteArray): ImageModel {
        val model = ImageModel(name=name, data = data)
        return imageJpaRepository.save(model)
    }

    fun retrieveBlob(id: Long): ImageModel {
        val blob = imageJpaRepository.findById(id)
        return blob.get()
    }

    fun retrieveAllBlobs(): MutableList<ImageModel> {
        val blobs = imageJpaRepository.findAll()
        return blobs
    }

}