package com.example.communal.requestPage.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "requests")
data class Request(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val addressId: Long,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val date: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    val comment: String? = null
)