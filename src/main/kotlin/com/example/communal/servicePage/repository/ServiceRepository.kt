package com.example.communal.servicePage.repository

import com.example.communal.servicePage.model.ServiceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : JpaRepository<ServiceEntity, Long> {
    fun findByCategory(category: String): List<ServiceEntity>
}