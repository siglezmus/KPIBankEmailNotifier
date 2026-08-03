package com.kpibank.email_notifier.services

import com.kpibank.email_notifier.events.DepositCompletedEvent
import com.kpibank.email_notifier.events.TransferSentEvent
import com.kpibank.email_notifier.events.WithdrawalCompletedEvent
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailNotificationService(
    private val mailSender: JavaMailSender
) {

    fun sendTransferEmail(event: TransferSentEvent) {

        val message = SimpleMailMessage()

        message.setTo(event.data.from.email)
        message.subject = "Transfer completed"

        message.text = """
            Hello ${event.data.from.ownerName},

            Your transfer of ${event.data.amount} ${event.data.currency}
            to ${event.data.to.ownerName}
            was completed.

            Description:
            ${event.data.description ?: "N/A"}
        """.trimIndent()

        mailSender.send(message)
    }


    fun sendDepositEmail(event: DepositCompletedEvent) {

        val message = SimpleMailMessage()

        message.setTo(event.data.account.email)
        message.subject = "Deposit completed"

        message.text = """
            Hello ${event.data.account.ownerName},

            ${event.data.amount} ${event.data.currency}
            has been deposited into your account.

            Source:
            ${event.data.source}
        """.trimIndent()

        mailSender.send(message)
    }


    fun sendWithdrawalEmail(event: WithdrawalCompletedEvent) {

        val message = SimpleMailMessage()

        message.setTo(event.data.account.email)
        message.subject = "Withdrawal completed"

        message.text = """
            Hello ${event.data.account.ownerName},

            ${event.data.amount} ${event.data.currency}
            was withdrawn from your account.

            Destination:
            ${event.data.destination}
        """.trimIndent()

        mailSender.send(message)
    }
}