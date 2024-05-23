package com.cropyback.cropyback.controller

import com.cropyback.cropyback.api.kafka.producer.ImageEventProducer
import com.cropyback.cropyback.database.entities.user.ImageEntity
import com.cropyback.cropyback.service.ImageService
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.core.annotation.AliasFor
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.InvalidMediaTypeException
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.net.URLEncoder
import java.util.*

@RestController
@RequestMapping("/api")
class ImageController(
    private val gridFsTemplate: GridFsTemplate,
    private val imageEventProducer: ImageEventProducer
    ) {

    @Autowired
    lateinit var imageService: ImageService

    @PostMapping("/images")
    fun saveImage(
        @RequestParam("image") file: MultipartFile,
        @RequestParam("filename") filename: String,
        @RequestParam("user_id") user: String
    ): ImageEntity {

        val fileId = gridFsTemplate.store(
            file.inputStream,
            filename,
            file.contentType,
        ).toString()
        val image = ImageEntity(filename=filename, fileId = fileId)
        imageService.saveImage(image)

        imageEventProducer.sendUploadImageEvent(fileId, user)

        return image
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/images/{id}")
    fun getImage(@PathVariable id: String): ResponseEntity<ByteArrayResource> {
        val image = gridFsTemplate.findOne(Query(Criteria.where("_id").`is`(ObjectId(id))))

        return if (!image.equals(null)) {

/*            val contentType = image.metadata?.get("contentType")?.toString()
                ?.let { MediaType.valueOf(it) } ?: MediaType.APPLICATION_OCTET_STREAM*/

            val contentType = try {
                MediaType.parseMediaType(image.metadata?.get("contentType")?.toString() ?: "image/jpeg")
            } catch (e: InvalidMediaTypeException) {
                MediaType.IMAGE_JPEG // Default to JPEG if invalid
            }


            ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.filename + "\"")  // Add this line
                .body(ByteArrayResource(gridFsTemplate.getResource(image).inputStream.readBytes()))
        } else {
            ResponseEntity.notFound().build()
        }
    }
}