package com.kpibank.email_notifier.events

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class WithdrawalCompletedEvent(
    val eventId: UUID,
    val eventType: String,
    val eventVersion: Int,
    val occurredAt: OffsetDateTime,
    val producer: String,
    val data: WithdrawalCompletedData
)

data class WithdrawalCompletedData(
    val transactionId: UUID,
    val account: AccountOwner,
    val destination: String,
    val amount: BigDecimal,
    val currency: String
)