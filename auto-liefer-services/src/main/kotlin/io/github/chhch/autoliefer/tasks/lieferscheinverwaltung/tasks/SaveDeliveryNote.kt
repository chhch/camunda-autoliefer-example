package io.github.chhch.autoliefer.tasks.lieferscheinverwaltung.tasks

import io.github.chhch.autoliefer.tasks.lieferscheinverwaltung.model.DeliveryNote
import org.camunda.bpm.client.ExternalTaskClient
import java.util.*
import java.util.logging.Logger

class SaveDeliveryNote {

    companion object {
        private val LOGGER = Logger.getLogger(SaveDeliveryNote::class.java.name)

        val deliveryNotes = ArrayList<DeliveryNote>()
    }

    fun start() {
        LOGGER.info("${SaveDeliveryNote::class.simpleName} started")

        val client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .build()

        client.subscribe("save-delivery-note")
                .lockDuration(1_000)
                .handler { externalTask, externalTaskService ->
                    val deliveryNote = DeliveryNote(externalTask)
                    LOGGER.info("Store delivery note in ECM-System {$deliveryNote}")
                    deliveryNotes.add(deliveryNote)

                    externalTaskService.complete(externalTask)
                }
                .open()
    }
}
