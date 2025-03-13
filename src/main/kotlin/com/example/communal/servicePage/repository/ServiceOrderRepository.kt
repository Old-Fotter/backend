package com.example.communal.servicePage.repository

import com.example.communal.servicePage.model.ServiceOrder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceOrderRepository : JpaRepository<ServiceOrder, Long> {
    fun findByUserId(userId: String): List<ServiceOrder>
    fun findByUserIdOrderByDateTimeDesc(userId: String): List<ServiceOrder>
}