package com.kpibank.email_notifier.services

import com.kpibank.email_notifier.events.DepositCompletedEvent
import com.kpibank.email_notifier.events.TransferSentEvent
import com.kpibank.email_notifier.events.WithdrawalCompletedEvent
import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailNotificationService(
    private val mailSender: JavaMailSender
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun sendTransferEmail(event: TransferSentEvent) {

        val recipient = event.data.from.email

        logger.info(
            "Sending transfer notification email. transferId={}, recipient={}, amount={} {}",
            event.data.transferId,
            recipient,
            event.data.amount,
            event.data.currency
        )

        val message = SimpleMailMessage()

        message.setTo(recipient)
        message.subject = "Transfer completed"

        message.text = """
            Hello ${event.data.from.ownerName},

            Your transfer of ${event.data.amount} ${event.data.currency}
            to ${event.data.to.ownerName}
            was completed.

        """.trimIndent()

        mailSender.send(message)

        logger.info(
            "Transfer notification email sent successfully. transferId={}",
            event.data.transferId
        )
    }


    fun sendDepositEmail(event: DepositCompletedEvent) {

        val recipient = event.data.account.email

        logger.info(
            "Sending deposit notification email. transactionId={}, recipient={}, amount={} {}",
            event.data.transactionId,
            recipient,
            event.data.amount,
            event.data.currency
        )

        val message = SimpleMailMessage()

        message.setTo(recipient)
        message.subject = "Deposit completed"

        message.text = """
            Hello ${event.data.account.ownerName},

            ${event.data.amount} ${event.data.currency}
            has been deposited into your account.

            Source:
            ${event.data.source}
        """.trimIndent()

        mailSender.send(message)

        logger.info(
            "Deposit notification email sent successfully. transactionId={}",
            event.data.transactionId
        )
    }


    fun sendWithdrawalEmail(event: WithdrawalCompletedEvent) {

        val recipient = event.data.account.email

        logger.info(
            "Sending withdrawal notification email. transactionId={}, recipient={}, amount={} {}",
            event.data.transactionId,
            recipient,
            event.data.amount,
            event.data.currency
        )

        val message = SimpleMailMessage()

        message.setTo(recipient)
        message.subject = "Withdrawal completed"

        message.text = """
            Hello ${event.data.account.ownerName},

            ${event.data.amount} ${event.data.currency}
            was withdrawn from your account.

            Destination:
            ${event.data.destination}
        """.trimIndent()

        mailSender.send(message)

        logger.info(
            "Withdrawal notification email sent successfully. transactionId={}",
            event.data.transactionId
        )
    }
}