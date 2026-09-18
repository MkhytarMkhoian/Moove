package com.moove.tickets.data.local.dto

import com.moove.tickets.domain.model.TicketReceipt
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TicketReceiptDTO(
    @Json(name = "transaction_id")
    val transactionId: String,
)

fun TicketReceiptDTO.asDomain() = TicketReceipt(
    transactionId = transactionId,
)
