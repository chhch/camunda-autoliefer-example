package io.github.chhch.autoliefer.tasks.lieferscheinverwaltung

import io.github.chhch.autoliefer.tasks.lieferscheinverwaltung.tasks.SaveDeliveryNote
import io.github.chhch.autoliefer.tasks.lieferscheinverwaltung.tasks.SendDeliveryNote

class Lieferscheinverwaltung {
    fun start() {
        SaveDeliveryNote().start()
        SendDeliveryNote().start()
    }
}