package com.example.communal.metersPage.controller

import com.example.communal.metersPage.model.MeterCategory
import com.example.communal.metersPage.model.MeterReading
import com.example.communal.metersPage.service.MeterReadingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/meter-readings")
class MeterReadingController(private val service: MeterReadingService) {

    @PostMapping
    fun submitReading(@RequestBody request: MeterReadingRequest): ResponseEntity<Any> {
        return try {
            val savedReading = service.submitReading(request.userId, request.meterId, request.currentValue)
            ResponseEntity.ok(savedReading)
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to ex.message))
        }
    }

    @GetMapping("/history-with-address/{userId}")
    fun getHistoryWithAddress(@PathVariable userId: Long): ResponseEntity<List<Map<String, Any>>> {
        val history = service.getReadingsHistoryWithAddress(userId)
        return ResponseEntity.ok(history)
    }

    @GetMapping("/{id}")
    fun getReadingById(@PathVariable id: Long): ResponseEntity<MeterReading> {
        val reading = service.getReadingById(id)
        return ResponseEntity.ok(reading)
    }

    @GetMapping("/history/{userId}")
    fun getHistory(@PathVariable userId: Long): ResponseEntity<List<MeterReading>> {
        val history = service.getReadingsHistory(userId)
        return ResponseEntity.ok(history)
    }
}

data class MeterReadingRequest(
    val userId: Long,
    val meterId: Long,
    val currentValue: Double
)
