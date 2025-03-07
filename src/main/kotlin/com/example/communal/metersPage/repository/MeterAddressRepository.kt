package com.example.communal.metersPage.repository

import com.example.communal.metersPage.model.MeterAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MeterAddressRepository : JpaRepository<MeterAddress, Long> {
    fun findByUserId(userId: Long): List<MeterAddress>
}

