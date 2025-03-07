package com.example.communal.metersPage.controller

import com.example.communal.metersPage.model.Meter
import com.example.communal.metersPage.model.MeterAddress
import com.example.communal.metersPage.model.MeterCategory
import com.example.communal.metersPage.repository.MeterAddressRepository
import com.example.communal.metersPage.repository.MeterRepository
import com.example.communal.metersPage.repository.MeterReadingRepository

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/meters")
class MeterController(
    private val meterRepository: MeterRepository,
    private val meterReadingRepository: MeterReadingRepository,
    private val addressRepository: MeterAddressRepository
) {

    @GetMapping("/addresses/{userId}")
    fun getUserAddresses(@PathVariable userId: Long = 1): ResponseEntity<List<com.example.communal.metersPage.model.MeterAddress>> {
        return ResponseEntity.ok(addressRepository.findByUserId(userId))
    }

    @GetMapping("/address/{addressId}")
    fun getMetersByAddress(@PathVariable addressId: Long): ResponseEntity<List<Meter>> {
        return ResponseEntity.ok(meterRepository.findByAddressId(addressId))
    }
    @PostMapping("/addresses")
    fun addAddress(@RequestBody request: AddressRequest): ResponseEntity<MeterAddress> {
        val newAddress = MeterAddress(userId = 1, address = request.address)
        val savedAddress = addressRepository.save(newAddress)

        val meters = listOf(
            Meter(userId = savedAddress.userId, addressId = savedAddress.id!!, meterCategory = MeterCategory.GAS),
            Meter(userId = savedAddress.userId, addressId = savedAddress.id!!, meterCategory = MeterCategory.HOT_WATER),
            Meter(userId = savedAddress.userId, addressId = savedAddress.id!!, meterCategory = MeterCategory.COLD_WATER)
        )
        meterRepository.saveAll(meters)

        return ResponseEntity.ok(savedAddress)
    }

}

data class AddressRequest(val address: String)

data class MeterRequest(
    val userId: Long,
    val address: String,
    val meterCategory: MeterCategory,
    val initialValue: Double
)
