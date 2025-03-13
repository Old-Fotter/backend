package com.example.communal.paymentPage

import com.example.communal.metersPage.model.MeterCategory
import com.example.communal.metersPage.repository.MeterAddressRepository
import com.example.communal.metersPage.repository.MeterReadingRepository
import com.example.communal.metersPage.repository.MeterRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PaymentService(
    private val meterRepository: MeterRepository,
    private val meterReadingRepository: MeterReadingRepository,
    private val meterAddressRepository: MeterAddressRepository,
    private val paymentRepository: PaymentRepository
) {
    private val GAS_TARIFF = 5.0
    private val HOT_WATER_TARIFF = 2.0
    private val COLD_WATER_TARIFF = 1.5

    fun calculateAndSavePayments(userId: String): List<Payment> {
        val payments = mutableListOf<Payment>()
        val meters = meterRepository.findByUserId(userId)
        for (meter in meters) {
            val reading = meterReadingRepository.findLatestByUserAndCategory(userId, meter.meterCategory)
            if (reading != null) {

                if (!paymentRepository.existsByReadingId(reading.id!!)) {
                    val difference = reading.currentValue - reading.previousValue
                    val tariff = when (meter.meterCategory) {
                        MeterCategory.GAS -> GAS_TARIFF
                        MeterCategory.HOT_WATER -> HOT_WATER_TARIFF
                        MeterCategory.COLD_WATER -> COLD_WATER_TARIFF
                    }
                    val amount = difference * tariff
                    val addressObj = meterAddressRepository.findById(meter.addressId).orElse(null)
                    val addressStr = addressObj?.address ?: "Не указан"

                    val payment = Payment(
                        readingId = reading.id!!,
                        userId = userId,
                        address = addressStr,
                        meterCategory = meter.meterCategory,
                        difference = difference,
                        tariff = tariff,
                        amount = amount,
                        paid = false,
                        createdAt = LocalDateTime.now()
                    )
                    payments.add(payment)
                }
            }
        }
        return paymentRepository.saveAll(payments)
    }

    fun getPayments(userId: String): List<Payment> {
        return paymentRepository.findByUserId(userId)
    }

    fun markPaymentAsPaid(paymentId: Long): Payment {
        val payment = paymentRepository.findById(paymentId)
            .orElseThrow { RuntimeException("Payment not found") }
        payment.paid = true
        return paymentRepository.save(payment)
    }
}
