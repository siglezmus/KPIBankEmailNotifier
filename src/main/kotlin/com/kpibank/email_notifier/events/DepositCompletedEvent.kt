package com.kpibank.email_notifier.events

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class DepositCompletedEvent(
    val eventId: UUID,
    val eventType: String,
    val eventVersion: Int,
    val occurredAt: OffsetDateTime,
    val producer: String,
    val data: DepositCompletedData
)

data class DepositCompletedData(
    val transactionId: UUID,
    val account: AccountOwner,
    val source: String,
    val amount: BigDecimal,
    val balance: BigDecimal,
    val currency: String
)