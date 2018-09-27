package io.github.chhch.autoliefer.tasks.zuordnung.model

import io.github.chhch.autoliefer.tasks.api.TypedVariable
import org.camunda.bpm.client.task.ExternalTask

data class DeliveryNote(
        val id: String,
        val amount: String,
        val approved: Boolean,
        val comment: String
) {
    constructor(task: ExternalTask) : this(
            task.getVariable<String>("deliveryNote_id"),
            task.getVariable<String>("deliveryNote_amount") ?: "",
            task.getVariable<Boolean>("deliveryNote_approved") ?: false,
            task.getVariable<String>("deliveryNote_comment") ?: ""
            )

    fun asProcessVariables(): Map<String, TypedVariable> = mapOf(
            "deliveryNote_id" to TypedVariable(id, "String"),
            "deliveryNote_amount" to TypedVariable(amount, "String"),
            "deliveryNote_approved" to TypedVariable(approved.toString(), "Boolean"),
            "deliveryNote_comment" to TypedVariable(comment, "String")
    )

    fun asVariables(): Map<String, Any> = mapOf(
            "deliveryNote_id" to id,
            "deliveryNote_amount" to amount,
            "deliveryNote_approved" to approved,
            "deliveryNote_comment" to comment
    )
}