package io.github.chhch.autoliefer.tasks.rechnugsverwaltung.tasks

import io.github.chhch.autoliefer.tasks.rechnugsverwaltung.model.Invoice
import org.camunda.bpm.client.ExternalTaskClient
import java.util.*
import java.util.logging.Logger

class SaveInvoice {

    companion object {
        private val LOGGER = Logger.getLogger(SaveInvoice::class.java.name)
        val invoices = ArrayList<Invoice>()
    }

    fun start() {
        LOGGER.info("${SaveInvoice::class.simpleName} started")

        val client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .build()

        client.subscribe("save-invoice")
                .lockDuration(1_000)
                .handler { externalTask, externalTaskService ->
                    val invoice = Invoice(externalTask)
                    LOGGER.info("Store invoice in ECM-System {$invoice}")
                    invoices.add(invoice)

                    externalTaskService.complete(externalTask)
                }
                .open()
    }
}