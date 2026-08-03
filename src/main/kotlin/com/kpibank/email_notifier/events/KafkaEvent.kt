package com.kpibank.email_notifier.events

import java.time.OffsetDateTime
import java.util.UUID

data class KafkaEvent<T>(
    val eventId: UUID,
    val eventType: String,
    val eventVersion: Int,
    val occurredAt: OffsetDateTime,
    val producer: String,
    val data: T
)