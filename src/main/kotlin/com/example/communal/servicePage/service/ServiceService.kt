package com.example.communal.servicePage.service

import com.example.communal.servicePage.model.ServiceEntity
import com.example.communal.servicePage.model.ServiceOrder
import com.example.communal.servicePage.repository.ServiceOrderRepository
import com.example.communal.servicePage.repository.ServiceRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ServiceService(
    private val serviceRepository: ServiceRepository,
    private val serviceOrderRepository: ServiceOrderRepository
) {

    fun getAllServices(): List<ServiceEntity> {
        return serviceRepository.findAll()
    }

    fun getServicesByCategory(category: String): List<ServiceEntity> {
        return serviceRepository.findByCategory(category)
    }

    fun submitServiceOrder(userId: String, serviceId: Long, addressId: Long, dateTime: LocalDateTime, comment: String? = null): ServiceOrder {
        val serviceOrder = ServiceOrder(
            userId = userId,
            serviceId = serviceId,
            addressId = addressId,
            dateTime = dateTime,
            comment = comment
        )
        return serviceOrderRepository.save(serviceOrder)
    }

    fun getOrderHistory(userId: String): List<ServiceOrder> {
        return serviceOrderRepository.findByUserIdOrderByDateTimeDesc(userId)
    }

    fun getServiceById(serviceId: Long): ServiceEntity {
        return serviceRepository.findById(serviceId)
            .orElseThrow { RuntimeException("Услуга не найдена") }
    }
}