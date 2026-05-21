package com.nullpointercats.sys.adopta.config

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@RestController
@RequestMapping("/photos")
class FileUploadController {

    @Value("\${file.upload-dir}")
    lateinit var uploadDir: String

    @PostMapping("/upload")
    fun uploadPhoto(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Map<String, String>> {

        if (file.isEmpty) return ResponseEntity.badRequest().build()

        val allowedTypes = listOf("image/jpeg", "image/png", "image/webp")
        if (file.contentType !in allowedTypes) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Only JPG, PNG and WEBP allowed"))
        }

        val uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir).toAbsolutePath()
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath)

        val extension = file.originalFilename?.substringAfterLast(".") ?: "jpg"
        val fileName = "${UUID.randomUUID()}.$extension"
        val filePath = uploadPath.resolve(fileName)

        file.transferTo(filePath.toFile())

        val url = "http://localhost:8080/photos/$fileName"
        return ResponseEntity.ok(mapOf("url" to url))
    }

    @GetMapping("/{filename}")
    fun getPhoto(@PathVariable filename: String, response: HttpServletResponse) {
        val uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir).toAbsolutePath()
        val filePath = uploadPath.resolve(filename)

        if (!Files.exists(filePath)) {
            response.status = 404
            return
        }

        val contentType = Files.probeContentType(filePath) ?: "image/jpeg"
        response.contentType = contentType
        response.outputStream.use { out ->
            filePath.toFile().inputStream().use { it.copyTo(out) }
        }
    }
}