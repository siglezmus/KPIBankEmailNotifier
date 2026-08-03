package com.kpibank.email_notifier

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmailNotifierApplication

fun main(args: Array<String>) {
	runApplication<EmailNotifierApplication>(*args)
}
