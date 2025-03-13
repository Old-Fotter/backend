package com.example.communal.requestPage.controller

import com.example.communal.requestPage.model.Request
import com.example.communal.requestPage.service.RequestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/requests")
class RequestController(private val service: RequestService) {

    @PostMapping
    fun submitRequest(@RequestBody request: RequestRequest): ResponseEntity<Request> {
        val savedRequest = service.submitRequest(
            userId = request.userId,
            addressId = request.addressId,
            title = request.title,
            comment = request.comment
        )
        return ResponseEntity.ok(savedRequest)
    }

    @GetMapping("/history/{userId}")
    fun getRequestHistory(@PathVariable userId: String): ResponseEntity<List<Request>> {
        val history = service.getRequestHistory(userId)
        return ResponseEntity.ok(history)
    }

    @GetMapping("/{id}")
    fun getRequestById(@PathVariable id: Long): ResponseEntity<Request> {
        val request = service.getRequestById(id)
        return ResponseEntity.ok(request)
    }
}

data class RequestRequest(
    val userId: String,
    val addressId: Long,
    val title: String,
    val comment: String? = null
)