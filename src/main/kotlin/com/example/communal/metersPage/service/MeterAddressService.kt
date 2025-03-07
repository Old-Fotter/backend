package com.example.communal.metersPage.service

import com.example.communal.metersPage.model.Meter
import com.example.communal.metersPage.model.MeterAddress
import com.example.communal.metersPage.model.MeterCategory
import com.example.communal.metersPage.repository.MeterAddressRepository
import com.example.communal.metersPage.repository.MeterRepository
import org.springframework.stereotype.Service

@Service
class MeterAddressService(
    private val meterAddressRepository: MeterAddressRepository,
    private val meterRepository: MeterRepository
) {
    fun saveAddress(address: MeterAddress): MeterAddress {
        val savedAddress = meterAddressRepository.save(address)

        val meters = listOf(
            Meter(userId = address.userId, addressId = savedAddress.id!!, meterCategory = MeterCategory.GAS),
            Meter(userId = address.userId, addressId = savedAddress.id!!, meterCategory = MeterCategory.HOT_WATER),
            Meter(userId = address.userId, addressId = savedAddress.id!!, meterCategory = MeterCategory.COLD_WATER)
        )
        meterRepository.saveAll(meters)

        return savedAddress
    }
}

