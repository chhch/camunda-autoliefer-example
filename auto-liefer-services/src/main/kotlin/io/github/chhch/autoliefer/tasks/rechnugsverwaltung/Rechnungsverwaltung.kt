package io.github.chhch.autoliefer.tasks.rechnugsverwaltung

import io.github.chhch.autoliefer.tasks.rechnugsverwaltung.tasks.SaveInvoice
import io.github.chhch.autoliefer.tasks.rechnugsverwaltung.tasks.SendInvoice

class Rechnungsverwaltung {
    fun start() {
        SaveInvoice().start()
        SendInvoice().start()
    }
}