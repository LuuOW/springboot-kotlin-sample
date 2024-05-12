package com.cropyback.cropyback.controller

import com.cropyback.cropyback.authentication.utils.CryptoUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpResponse


@RestController
@RequestMapping("/api")
class ResourceController {
    @GetMapping("/home")
    fun homeEndpoint(): ResponseEntity<String> {
        return ResponseEntity.ok("Welcome to the home page")
    }
}