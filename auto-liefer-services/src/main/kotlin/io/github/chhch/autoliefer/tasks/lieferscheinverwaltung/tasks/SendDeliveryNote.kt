package io.github.chhch.autoliefer.tasks.lieferscheinverwaltung.tasks

import io.github.chhch.autoliefer.tasks.api.CamundaService
import io.github.chhch.autoliefer.tasks.api.MessageBody
import io.github.chhch.autoliefer.tasks.api.TypedVariable
import io.github.chhch.autoliefer.tasks.lieferscheinverwaltung.model.DeliveryNote
import org.camunda.bpm.client.ExternalTaskClient
import java.util.*
import java.util.logging.Logger

class SendDeliveryNote {

    companion object {
        private val LOGGER = Logger.getLogger(SendDeliveryNote::class.java.name)
    }

    fun start() {
        LOGGER.info("${SendDeliveryNote::class.simpleName} started")

        val client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .build()

        client.subscribe("send-delivery-note")
                .lockDuration(1_000)
                .handler { externalTask, externalTaskService ->
                    val deliveryNote = DeliveryNote(externalTask)
                    val setCorrelationId = externalTask.getVariable<String>("replyCorrelationId") ?: UUID.randomUUID().toString()
                    val processVariables = mutableMapOf<String, TypedVariable>().apply {
                        putAll(deliveryNote.asProcessVariables())
                        put("replyCorrelationId", TypedVariable(setCorrelationId, "String"))
                    }

                    val messageName = externalTask.getVariable<String>("messageName")
                    val reply = externalTask.getVariable<String>("reply")?.toBoolean() ?: false

                    val body = if (reply) {
                        val sendToCorrelationId = externalTask.getVariable<String>("replyCorrelationId")
                        LOGGER.info("Send delivery note {$deliveryNote} back to $messageName ($sendToCorrelationId)")
                        val replyCorrelationIdProperty = mapOf("replyCorrelationId" to TypedVariable(sendToCorrelationId, "String"))
                        MessageBody(messageName, processVariables, replyCorrelationIdProperty)
                    } else {
                        LOGGER.info("Send delivery note {$deliveryNote} to $messageName")
                        MessageBody(messageName, processVariables)
                    }
                    CamundaService.sendMessage(body).execute()

                    val taskVariables = externalTask.allVariables
                    taskVariables.putIfAbsent("replyCorrelationId", setCorrelationId)
                    externalTaskService.complete(externalTask,taskVariables)
                }.open()
    }
}