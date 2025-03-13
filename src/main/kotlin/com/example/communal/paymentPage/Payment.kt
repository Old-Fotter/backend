package com.example.communal.paymentPage

import com.example.communal.metersPage.model.MeterCategory
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "payments")
data class Payment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val readingId: Long,
    val userId: String,
    val address: String,
    @Enumerated(EnumType.STRING)
    val meterCategory: MeterCategory,
    val difference: Double,
    val tariff: Double,
    val amount: Double,
    var paid: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
