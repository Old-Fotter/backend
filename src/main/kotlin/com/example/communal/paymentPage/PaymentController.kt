package com.example.communal.paymentPage

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payments")
class PaymentController(private val paymentService: PaymentService) {

    @GetMapping("/{userId}")
    fun getPayments(@PathVariable userId: String): ResponseEntity<List<Payment>> {
        val payments = paymentService.getPayments(userId)
        return ResponseEntity.ok(payments)
    }

    @PostMapping("/{paymentId}/pay")
    fun markPaymentAsPaid(@PathVariable paymentId: Long): ResponseEntity<Payment> {
        val updatedPayment = paymentService.markPaymentAsPaid(paymentId)
        return ResponseEntity.ok(updatedPayment)
    }
}
