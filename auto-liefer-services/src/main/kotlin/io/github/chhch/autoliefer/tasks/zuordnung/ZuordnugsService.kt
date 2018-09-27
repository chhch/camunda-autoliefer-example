package io.github.chhch.autoliefer.tasks.zuordnung

import io.github.chhch.autoliefer.tasks.zuordnung.tasks.*

class ZuordnugsService {
    fun start() {
        BookInvoiceAmount().start()
        CreateReference().start()
        DoBanktransfer().start()
        EscalationInform().start()
        SaveOrder().start()
    }
}