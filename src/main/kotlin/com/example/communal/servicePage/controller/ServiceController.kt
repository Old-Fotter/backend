package com.example.communal.servicePage.controller

import com.example.communal.servicePage.model.ServiceEntity
import com.example.communal.servicePage.model.ServiceOrder
import com.example.communal.servicePage.service.ServiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/services")
class ServiceController(private val service: ServiceService) {

    @GetMapping
    fun getAllServices(): ResponseEntity<List<ServiceEntity>> {
        return ResponseEntity.ok(service.getAllServices())
    }

    @GetMapping("/category/{category}")
    fun getServicesByCategory(@PathVariable category: String): ResponseEntity<List<ServiceEntity>> {
        return ResponseEntity.ok(service.getServicesByCategory(category))
    }

    @PostMapping("/orders")
    fun submitServiceOrder(@RequestBody order: ServiceOrderRequest): ResponseEntity<ServiceOrder> {
        val savedOrder = service.submitServiceOrder(
            userId = order.userId,
            serviceId = order.serviceId,
            addressId = order.addressId,
            dateTime = order.dateTime,
            comment = order.comment
        )
        return ResponseEntity.ok(savedOrder)
    }

    @GetMapping("/orders/history/{userId}")
    fun getOrderHistory(@PathVariable userId: Long): ResponseEntity<List<ServiceOrder>> {
        return ResponseEntity.ok(service.getOrderHistory(userId))
    }
    @GetMapping("/{serviceId}")
    fun getServiceById(@PathVariable serviceId: Long): ResponseEntity<ServiceEntity> {
        val serviceEntity = service.getServiceById(serviceId)
        return ResponseEntity.ok(serviceEntity)
    }

}

data class ServiceOrderRequest(
    val userId: Long,
    val serviceId: Long,
    val addressId: Long,
    val dateTime: LocalDateTime,
    val comment: String? = null
)