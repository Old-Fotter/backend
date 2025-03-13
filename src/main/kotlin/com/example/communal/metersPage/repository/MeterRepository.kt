package com.example.communal.metersPage.repository

import com.example.communal.metersPage.model.Meter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MeterRepository : JpaRepository<Meter, Long> {
    fun findByUserId(userId: String): List<Meter>
    fun findByAddressId(addressId: Long): List<Meter>
}

