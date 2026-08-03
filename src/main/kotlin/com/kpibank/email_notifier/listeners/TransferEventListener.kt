package com.kpibank.email_notifier.listeners

import com.kpibank.email_notifier.events.*
import com.kpibank.email_notifier.services.EmailNotificationService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

@Component
class TransferEventListener(
    private val objectMapper: ObjectMapper,
    private val emailNotificationService: EmailNotificationService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["bank.transfers"])
    fun consume(message: String) {

        logger.info("Received Kafka message: {}", message)

        val event: Map<String, Any> =
            objectMapper.readValue(
                message,
                object : TypeReference<Map<String, Any>>() {}
            )

        when (event["eventType"]) {

            "TRANSFER_SENT" -> {
                val transferEvent =
                    objectMapper.convertValue(
                        event,
                        TransferSentEvent::class.java
                    )

                logger.info(
                    "Processing transfer notification for {}",
                    transferEvent.data.from.email
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

                logger.info(
                    "Processing deposit notification for {}",
                    depositEvent.data.account.email
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

                logger.info(
                    "Processing withdrawal notification for {}",
                    withdrawalEvent.data.account.email
                )

                emailNotificationService.sendWithdrawalEmail(
                    withdrawalEvent
                )
            }

            else -> {
                logger.warn(
                    "Unknown Kafka event type: {}",
                    event["eventType"]
                )
            }
        }
    }
}