package io.github.chhch.autoliefer.tasks.zuordnung.tasks

import io.github.chhch.autoliefer.tasks.zuordnung.model.Invoice
import org.camunda.bpm.client.ExternalTaskClient
import java.util.logging.Logger

class BookInvoiceAmount {

    companion object {
        private val LOGGER = Logger.getLogger(BookInvoiceAmount::class.java.name)
    }

    fun start() {
        LOGGER.info("${BookInvoiceAmount::class.simpleName} started")

        val client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .build()

        client.subscribe("book-invoice-amount")
                .lockDuration(1_000)
                .handler { externalTask, externalTaskService ->
                    val invoice = Invoice(externalTask)
                    LOGGER.info("Book invoice amount ${invoice.amount} with the ERP-System from the financial accounting")
                    externalTaskService.complete(externalTask)
                }
                .open()
    }
}