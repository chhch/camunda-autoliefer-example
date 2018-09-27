package io.github.chhch.autoliefer.tasks.zuordnung.tasks

import io.github.chhch.autoliefer.tasks.zuordnung.model.Invoice
import org.camunda.bpm.client.ExternalTaskClient
import java.util.logging.Logger

class DoBanktransfer {

    companion object {
        private val LOGGER = Logger.getLogger(DoBanktransfer::class.java.name)
    }

    fun start() {
        LOGGER.info("${DoBanktransfer::class.simpleName} started")

        val client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .build()

        client.subscribe("do-bank-transfer")
                .lockDuration(1_000)
                .handler { externalTask, externalTaskService ->
                    val invoice = Invoice(externalTask)
                    LOGGER.info("Do bank transfer for $invoice.")
                    externalTaskService.complete(externalTask)
                }
                .open()
    }
}