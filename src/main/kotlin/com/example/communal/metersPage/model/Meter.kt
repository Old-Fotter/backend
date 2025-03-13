package com.example.communal.metersPage.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "meters")
data class Meter(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val userId: String,
    val addressId: Long,

    @Enumerated(EnumType.STRING)
    val meterCategory: MeterCategory,

    var previousValue: Double = 0.0,
    var currentValue: Double = 0.0,

    var lastSubmissionDate: LocalDateTime? = null
)

enum class MeterCategory {
    GAS, HOT_WATER, COLD_WATER
}
