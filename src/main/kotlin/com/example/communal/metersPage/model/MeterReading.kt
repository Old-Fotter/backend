package com.example.communal.metersPage.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "meter_readings")
data class MeterReading(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val userId: String,
    val addressId: Long,
    @Enumerated(EnumType.STRING)
    val meterCategory: MeterCategory,
    val previousValue: Double,
    val currentValue: Double,
    val submittedAt: LocalDateTime = LocalDateTime.now()
)



