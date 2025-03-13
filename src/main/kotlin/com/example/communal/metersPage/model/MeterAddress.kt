package com.example.communal.metersPage.model

import jakarta.persistence.*

@Entity
@Table(name = "meter_addresses")
data class MeterAddress(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val userId: String,
    val address: String
)

