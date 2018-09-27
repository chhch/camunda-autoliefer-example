package io.github.chhch.autoliefer.tasks.zuordnung.model

import io.github.chhch.autoliefer.tasks.api.TypedVariable
import org.camunda.bpm.client.task.ExternalTask

data class Invoice(
        val id: String,
        val deliveryNoteId: String,
        val asset: String,
        val amount: String,
        val approved: Boolean,
        val comment: String
) {
    constructor(task: ExternalTask) : this(
            task.getVariable<String>("invoice_id"),
            task.getVariable<String>("invoice_deliveryNoteId"),
            task.getVariable<String>("invoice_asset"),
            task.getVariable<String>("invoice_amount"),
            task.getVariable<Boolean>("invoice_approved") ?: false,
            task.getVariable<String>("invoice_comment") ?: ""
    )

    fun asProcessVariables(): Map<String, TypedVariable> = mapOf(
            "invoice_id" to TypedVariable(id, "String"),
            "invoice_deliveryNoteId" to TypedVariable(deliveryNoteId, "String"),
            "invoice_asset" to TypedVariable(asset, "String"),
            "invoice_amount" to TypedVariable(amount, "String"),
            "invoice_approved" to TypedVariable(approved.toString(), "Boolean"),
            "invoice_comment" to TypedVariable(comment, "String")
    )

    fun asVariables(): Map<String, Any> = mapOf(
            "invoice_id" to id,
            "invoice_deliveryNoteId" to deliveryNoteId,
            "invoice_asset" to asset,
            "invoice_amount" to amount,
            "invoice_approved" to approved,
            "invoice_comment" to comment
    )
}