package io.github.chhch.autoliefer.tasks.zuordnung.tasks

import org.camunda.bpm.client.ExternalTaskClient
import java.util.logging.Logger

class EscalationInform {

    companion object {
        private val LOGGER = Logger.getLogger(EscalationInform::class.java.name)
    }

    fun start() {
        LOGGER.info("${EscalationInform::class.simpleName} started")

        val client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .build()

        client.subscribe("escalation-inform")
                .lockDuration(1_000)
                .handler { externalTask, externalTaskService ->
                    val stakeholder = externalTask.getVariable<String>("escalation_stakeholder")
                    val comment = externalTask.getVariable<String>("order_comment")
                    LOGGER.info("Inform '$stakeholder' about escalation with message '$comment'.")
                    externalTaskService.complete(externalTask)
                }
                .open()
    }
}