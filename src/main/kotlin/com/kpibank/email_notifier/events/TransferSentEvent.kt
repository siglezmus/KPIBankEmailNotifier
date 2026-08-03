package com.kpibank.email_notifier.events

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class TransferSentEvent(
    val eventId: UUID,
    val eventType: String,
    val eventVersion: Int,
    val occurredAt: OffsetDateTime,
    val producer: String,
    val data: TransferSentData
)

data class TransferSentData(
    val transferId: UUID,
    val from: AccountOwner,
    val to: AccountOwner,
    val amount: BigDecimal,
    val currency: String,
    val description: String?
)