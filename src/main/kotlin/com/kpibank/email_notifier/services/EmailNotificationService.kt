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

    private val sentTransfers = mutableSetOf<String>()

    fun sendTransferEmail(event: TransferSentEvent) {

        val transferId = event.data.transferId

        if (sentTransfers.add("$transferId-sender")) {
            sendSenderTransferEmail(event)
        }

        Thread.sleep(20000)

        if (sentTransfers.add("$transferId-receiver")) {
            sendReceiverTransferEmail(event)
        }
    }

    private fun sendSenderTransferEmail(event: TransferSentEvent) {

        val sender = event.data.from

        val message = SimpleMailMessage()

        message.setTo(sender.email)
        message.subject = "Transfer completed"

        message.text = """
        Hello ${sender.ownerName},

        Your transfer of ${event.data.amount} ${event.data.currency}
        to ${event.data.to.ownerName}
        was completed successfully.

        Transfer ID:
        ${event.data.transferId}

    """.trimIndent()

        mailSender.send(message)

        logger.info(
            "Sender transfer email sent. transferId={}, recipient={}",
            event.data.transferId,
            sender.email
        )
    }

    private fun sendReceiverTransferEmail(event: TransferSentEvent) {

        val receiver = event.data.to

        val message = SimpleMailMessage()

        message.setTo(receiver.email)
        message.subject = "You received a transfer"

        message.text = """
        Hello ${receiver.ownerName},

        You received ${event.data.amount} ${event.data.currency}
        from ${event.data.from.ownerName}.

        Transfer ID:
        ${event.data.transferId}

    """.trimIndent()

        mailSender.send(message)

        logger.info(
            "Receiver transfer email sent. transferId={}, recipient={}",
            event.data.transferId,
            receiver.email
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