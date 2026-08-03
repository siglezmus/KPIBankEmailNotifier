package com.kpibank.email_notifier.listeners

import com.kpibank.email_notifier.events.DepositCompletedEvent
import com.kpibank.email_notifier.events.TransferSentEvent
import com.kpibank.email_notifier.events.WithdrawalCompletedEvent
import com.kpibank.email_notifier.services.EmailNotificationService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import com.fasterxml.jackson.databind.ObjectMapper

@Component
class TransferEventListener(
    private val objectMapper: ObjectMapper,
    private val emailNotificationService: EmailNotificationService
) {

    @KafkaListener(topics = ["bank.transfers"])
    fun consume(event: Map<String, Any>) {

        when (event["eventType"]) {

            "TRANSFER_SENT" -> {
                val transferEvent =
                    objectMapper.convertValue(
                        event,
                        TransferSentEvent::class.java
                    )

                emailNotificationService.sendTransferEmail(
                    transferEvent
                )
            }

            "DEPOSIT_COMPLETED" -> {
                val depositEvent =
                    objectMapper.convertValue(
                        event,
                        DepositCompletedEvent::class.java
                    )

                emailNotificationService.sendDepositEmail(
                    depositEvent
                )
            }

            "WITHDRAWAL_COMPLETED" -> {
                val withdrawalEvent =
                    objectMapper.convertValue(
                        event,
                        WithdrawalCompletedEvent::class.java
                    )

                emailNotificationService.sendWithdrawalEmail(
                    withdrawalEvent
                )
            }

            else -> {
                // ignore unknown events
            }
        }
    }
}