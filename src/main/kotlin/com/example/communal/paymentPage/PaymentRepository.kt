package com.example.communal.paymentPage

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : JpaRepository<Payment, Long> {
    fun findByUserId(userId: Long): List<Payment>
    fun existsByReadingId(readingId: Long): Boolean
}
