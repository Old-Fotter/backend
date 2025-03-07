package com.example.communal.paymentPage

import com.example.communal.metersPage.model.MeterCategory

data class PaymentDTO(
    val address: String,
    val meterCategory: MeterCategory,
    val difference: Double,
    val tariff: Double,
    val amount: Double
)
