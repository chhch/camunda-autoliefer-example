package io.github.chhch.autoliefer.tasks

import io.github.chhch.autoliefer.tasks.lieferscheinverwaltung.Lieferscheinverwaltung
import io.github.chhch.autoliefer.tasks.rechnugsverwaltung.Rechnungsverwaltung
import io.github.chhch.autoliefer.tasks.zuordnung.ZuordnugsService

fun main(args: Array<String>) {
    Lieferscheinverwaltung().start()
    Rechnungsverwaltung().start()
    ZuordnugsService().start()
}