package com.kpibank.email_notifier.events

import java.util.UUID

data class AccountOwner(
    val accountId: UUID,
    val ownerName: String,
    val email: String
)