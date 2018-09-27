package io.github.chhch.autoliefer.tasks.zuordnung.tasks

import io.github.chhch.autoliefer.tasks.zuordnung.model.Order
import org.camunda.bpm.client.ExternalTaskClient
import java.util.*
import java.util.logging.Logger

class SaveOrder {

    companion object {
        private val LOGGER = Logger.getLogger(SaveOrder::class.java.name)

        val orders = ArrayList<Order>()
    }

    fun start() {
        LOGGER.info("${SaveOrder::class.simpleName} started")

        val client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .build()

        client.subscribe("save-order")
                .lockDuration(1_000)
                .handler { externalTask, externalTaskService -> 
                    val order = Order(externalTask)
                    LOGGER.info("Store order in ECM-System {$order}")
                    orders.add(order)

                    externalTaskService.complete(externalTask)
                }
                .open()
    }
}