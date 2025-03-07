package com.example.communal.servicePage.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "service_orders")
data class ServiceOrder(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val serviceId: Long,

    @Column(nullable = false)
    val addressId: Long,

    @Column(nullable = false)
    val dateTime: LocalDateTime,

    @Column(nullable = true)
    val comment: String? = null,

    @Column(nullable = false)
    val status: String = "PENDING" // Статусы: "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"
)