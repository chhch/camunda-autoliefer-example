package io.github.chhch.autoliefer.tasks.zuordnung.tasks

import io.github.chhch.autoliefer.tasks.lieferscheinverwaltung.tasks.SaveDeliveryNote
import io.github.chhch.autoliefer.tasks.rechnugsverwaltung.tasks.SaveInvoice
import io.github.chhch.autoliefer.tasks.zuordnung.model.DeliveryNote
import io.github.chhch.autoliefer.tasks.zuordnung.model.Invoice
import io.github.chhch.autoliefer.tasks.zuordnung.model.Order
import org.camunda.bpm.client.ExternalTaskClient
import java.util.logging.Logger

class CreateReference {

    companion object {
        private val LOGGER = Logger.getLogger(CreateReference::class.java.name)

        val tmpOrders = ArrayList<Order>()
    }

    fun start() {
        LOGGER.info("${CreateReference::class.simpleName} started")

        val client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .build()

        client.subscribe("create-reference")
                .lockDuration(1_000)
                .handler { externalTask, externalTaskService ->
                    val isInvoice = externalTask.getVariable<String>("invoice_deliveryNoteId") != null
                    val order = if (isInvoice) {
                        val invoice = Invoice(externalTask)
                        // the next line would be a rest request
                        val deliveryNote = SaveDeliveryNote.deliveryNotes.find { it.id == invoice.deliveryNoteId }
                        LOGGER.info("Receive invoice {$invoice}, found delivery note {$deliveryNote}")
                        if (deliveryNote != null) {
                            val myDeliveryNote = DeliveryNote( // uses their own model
                                    deliveryNote.id,
                                    deliveryNote.amount,
                                    deliveryNote.approved,
                                    deliveryNote.comment
                            )
                            Order(invoice, myDeliveryNote)
                        } else
                            null
                    } else {
                        val deliveryNote = DeliveryNote(externalTask)
                        // the next line would be a rest request
                        val invoice = SaveInvoice.invoices.find { it.deliveryNoteId == deliveryNote.id }
                        LOGGER.info("Receive delivery note {$deliveryNote}, found invoice {$invoice}")
                        if (invoice != null) {
                            val myInvoice = Invoice( // uses their own model
                                    invoice.id,
                                    invoice.deliveryNoteId,
                                    invoice.asset,
                                    invoice.amount,
                                    invoice.approved,
                                    invoice.comment
                            )
                            Order(myInvoice, deliveryNote)
                        } else
                            null
                    }

                    val allVariables = externalTask.allVariables
                    if (order != null) {
                        LOGGER.info("Reference found {$order}")
                        val orderAlreadyExists = tmpOrders
                                .any { it.invoice == order.invoice && it.deliveryNote == order.deliveryNote }
                        if (orderAlreadyExists) {
                            LOGGER.info("Order already exists {$order}. Do nothing.")
                            allVariables["order_referenceFound"] = false
                        } else {
                            tmpOrders.add(order)
                            allVariables.putAll(order.deliveryNote.asVariables())
                            allVariables.putAll(order.invoice.asVariables())
                            allVariables["order_referenceFound"] = true
                        }
                        externalTaskService.complete(externalTask, allVariables)
                    } else {
                        LOGGER.info("No reference found")
                        allVariables["order_referenceFound"] = false
                        externalTaskService.complete(externalTask, allVariables)
                    }
                }
                .open()
    }
}
