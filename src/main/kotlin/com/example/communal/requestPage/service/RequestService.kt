package com.example.communal.requestPage.service

import com.example.communal.requestPage.model.Request
import com.example.communal.requestPage.repository.RequestRepository
import org.springframework.stereotype.Service

@Service
class RequestService(private val requestRepository: RequestRepository) {

    fun submitRequest(userId: String, addressId: Long, title: String, comment: String? = null): Request {
        val request = Request(
            userId = userId,
            addressId = addressId,
            title = title,
            comment = comment
        )
        return requestRepository.save(request)
    }

    fun getRequestHistory(userId: String): List<Request> {
        return requestRepository.findByUserIdOrderByDateDesc(userId)
    }

    fun getRequestById(id: Long): Request {
        return requestRepository.findById(id)
            .orElseThrow { RuntimeException("Заявка не найдена") }
    }
}