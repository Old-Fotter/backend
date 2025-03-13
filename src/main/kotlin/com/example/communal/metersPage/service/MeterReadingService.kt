package com.example.communal.metersPage.service

import com.example.communal.metersPage.model.MeterCategory
import com.example.communal.metersPage.model.MeterReading
import com.example.communal.metersPage.repository.MeterReadingRepository
import com.example.communal.metersPage.repository.MeterRepository
import com.example.communal.paymentPage.PaymentService
import com.example.communal.metersPage.model.MeterAddress
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MeterReadingService(
    private val meterRepository: MeterRepository,
    private val meterReadingRepository: MeterReadingRepository,
    private val paymentService: PaymentService
) {
    fun submitReading(userId: String, meterId: Long, currentValue: Double): MeterReading {
        val meter = meterRepository.findById(meterId)
            .orElseThrow { IllegalArgumentException("Счетчик не найден") }

        if (currentValue < meter.currentValue) {
            throw IllegalArgumentException("Текущее значение не может быть меньше предыдущего!")
        }

        meter.previousValue = meter.currentValue
        meter.currentValue = currentValue
        meter.lastSubmissionDate = LocalDateTime.now()

        val newReading = meterReadingRepository.save(
            MeterReading(
                userId = userId,
                addressId = meter.addressId,
                meterCategory = meter.meterCategory,
                previousValue = meter.previousValue,
                currentValue = currentValue,
                submittedAt = LocalDateTime.now()
            )
        )

        paymentService.calculateAndSavePayments(userId)

        meterRepository.save(meter)
        return newReading
    }

    fun getReadingById(id: Long): MeterReading {
        return meterReadingRepository.findById(id)
            .orElseThrow { RuntimeException("Meter reading not found") }
    }

    fun getReadingsHistory(userId: String): List<MeterReading> {
        return meterReadingRepository.findByUserIdOrderBySubmittedAtDesc(userId)
    }

    fun getReadingsHistoryWithAddress(userId: String): List<Map<String, Any>> {
        val readingsWithAddress = meterReadingRepository.findHistoryWithAddress(userId)
        return readingsWithAddress.map { result ->
            val reading = result[0] as MeterReading
            val address = result[1] as MeterAddress
            mapOf(
                "id" to (reading.id as Any),
                "address" to (address.address as Any),
                "meterCategory" to (reading.meterCategory as Any),
                "previousValue" to (reading.previousValue as Any),
                "currentValue" to (reading.currentValue as Any),
                "submittedAt" to (reading.submittedAt as Any)
            )
        }
    }
}
