package com.example.communal.requestPage.repository

import com.example.communal.requestPage.model.Request
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestRepository : JpaRepository<Request, Long> {
    fun findByUserId(userId: String): List<Request>
    fun findByUserIdOrderByDateDesc(userId: String): List<Request>
}