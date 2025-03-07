package com.example.communal.servicePage.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "services")
data class ServiceEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val category: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val example: String,

    @Column(nullable = false)
    val price: BigDecimal
)