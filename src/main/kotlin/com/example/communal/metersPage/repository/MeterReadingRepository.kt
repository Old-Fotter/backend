package com.example.communal.metersPage.repository

import com.example.communal.metersPage.model.MeterReading
import com.example.communal.metersPage.model.MeterCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MeterReadingRepository : JpaRepository<MeterReading, Long> {
    fun findByUserId(userId: Long): List<MeterReading>
    @Query("SELECT m FROM MeterReading m WHERE m.userId = :userId AND m.meterCategory = :meterCategory ORDER BY m.submittedAt DESC LIMIT 1")
    fun findLatestByUserAndCategory(@Param("userId") userId: Long, @Param("meterCategory") meterCategory: MeterCategory): MeterReading?

    fun findByUserIdOrderBySubmittedAtDesc(userId: Long): List<MeterReading>

    @Query("""
    SELECT mr, ma 
    FROM MeterReading mr 
    JOIN MeterAddress ma ON mr.addressId = ma.id 
    WHERE mr.userId = :userId 
    ORDER BY mr.submittedAt DESC
""")
    fun findHistoryWithAddress(@Param("userId") userId: Long): List<Array<Any>>

}
