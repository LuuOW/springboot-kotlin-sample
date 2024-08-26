package com.cropyback.cropyback.controller

import com.cropyback.cropyback.api.kafka.producer.ImageEventProducer
import com.cropyback.cropyback.database.sql.service.ImageJpaService
import com.cropyback.cropyback.database.sql.user.ImageModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class ImageController(
    private val imageEventProducer: ImageEventProducer
    ) {

    @Autowired
    private lateinit var imageJpaService: ImageJpaService

    //lateinit var imageService: ImageService

    @PostMapping("/images")
    fun saveImage(
        @RequestParam("image") file: MultipartFile,
        @RequestParam("filename") filename: String,
        @RequestParam("user_id") user: String
    ) {
        imageJpaService.storeBlob(filename, data = file.bytes)
        //imageEventProducer.sendUploadImageEvent(fileId, user)
    }

    @GetMapping("/images")
    fun getAllImages(): ResponseEntity<MutableList<ImageModel>> {
        return ResponseEntity.ok().body(imageJpaService.retrieveAllBlobs())
    }

    @GetMapping("/image")
    fun getImage(@Param("id") id: String): ResponseEntity<ByteArrayResource> {
        val image = imageJpaService.retrieveBlob(id.toLong())

        if (image != null) { // Check if image is not null

            val contentType = MediaType.IMAGE_JPEG // Default content type

            val imageBytes = image.data // Access image data directly from the model

            val responseEntity = ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.name + "\"")
                .body(ByteArrayResource(imageBytes))

            return responseEntity
        } else {
            // Handle case where image is null (optional: return error or empty response)
            return ResponseEntity.notFound().build() // Example for a 404 Not Found
        }

    }
}