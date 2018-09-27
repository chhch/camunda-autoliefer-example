package io.github.chhch.autoliefer.tasks.rechnugsverwaltung.tasks

import io.github.chhch.autoliefer.tasks.api.CamundaService
import io.github.chhch.autoliefer.tasks.api.MessageBody
import io.github.chhch.autoliefer.tasks.api.TypedVariable
import io.github.chhch.autoliefer.tasks.rechnugsverwaltung.model.Invoice
import org.camunda.bpm.client.ExternalTaskClient
import java.util.*
import java.util.logging.Logger

class SendInvoice {

    companion object {
        private val LOGGER = Logger.getLogger(SendInvoice::class.java.name)
    }

    fun start() {
        LOGGER.info("${SendInvoice::class.simpleName} started")

        val client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .build()

        client.subscribe("send-invoice")
                .lockDuration(1_000)
                .handler { externalTask, externalTaskService ->
                    val invoice = Invoice(externalTask)
                    val setCorrelationId = externalTask.getVariable<String>("replyCorrelationId") ?: UUID.randomUUID().toString()
                    val processVariables = mutableMapOf<String, TypedVariable>().apply {
                        putAll(invoice.asProcessVariables())
                        put("replyCorrelationId", TypedVariable(setCorrelationId, "String"))
                    }

                    val messageName = externalTask.getVariable<String>("messageName")
                    val reply = externalTask.getVariable<String>("reply")?.toBoolean() ?: false

                    val body = if (reply) {
                        val sendToCorrelationId = externalTask.getVariable<String>("replyCorrelationId")
                        LOGGER.info("Send invoice {$invoice} back to $messageName ($sendToCorrelationId)")
                        val replyCorrelationIdProperty = mapOf("replyCorrelationId" to TypedVariable(sendToCorrelationId, "String"))
                        MessageBody(messageName, processVariables, replyCorrelationIdProperty)
                    } else {
                        LOGGER.info("Send invoice {$invoice} to $messageName")
                        MessageBody(messageName, processVariables)
                    }
                    CamundaService.sendMessage(body).execute()

                    val taskVariables = externalTask.allVariables
                    taskVariables.putIfAbsent("replyCorrelationId", setCorrelationId)
                    externalTaskService.complete(externalTask,taskVariables)
                }.open()
    }
}