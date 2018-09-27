package io.github.chhch.autoliefer.tasks.zuordnung.model

import org.camunda.bpm.client.task.ExternalTask

data class Order(
        val invoice: Invoice,
        val deliveryNote: DeliveryNote,
        val associationOk: Boolean = false,
        val comment: String = "",
        val isEscalated: Boolean = false
) {

    constructor(task: ExternalTask) : this(
            Invoice(task),
            DeliveryNote(task),
            task.getVariable<Boolean>("order_associationOk") ?: false,
            task.getVariable<String>("order_comment") ?: "",
            task.getVariable<Boolean>("order_isEscalated") ?: false
    )
}