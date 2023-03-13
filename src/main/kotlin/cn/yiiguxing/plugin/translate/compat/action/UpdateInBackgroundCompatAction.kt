@file:Suppress("unused")

package cn.yiiguxing.plugin.translate.compat.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.util.NlsActions
import com.intellij.util.ui.EDT
import java.util.function.Supplier
import javax.swing.Icon

abstract class UpdateInBackgroundCompatAction : AnAction {
    constructor() : super()

    constructor(icon: Icon?) : super(icon)

    constructor(text: @NlsActions.ActionText String?) : super(text)

    constructor(dynamicText: Supplier<String?>) : super(dynamicText)

    constructor(
        text: @NlsActions.ActionText String?,
        description: @NlsActions.ActionDescription String?,
        icon: Icon?
    ) : super(text, description, icon)

    constructor(dynamicText: Supplier<String?>, icon: Icon?) : super(dynamicText, icon)

    constructor(
        dynamicText: Supplier<String?>,
        dynamicDescription: Supplier<String?>,
        icon: Icon?
    ) : super(dynamicText, dynamicDescription, icon)

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    protected fun <T> computeOnEDTUpdateSession(
        event: AnActionEvent,
        operationName: String,
        supplier: Supplier<out T>
    ): T {
        if (ApplicationInfo.getInstance().build.baselineVersion >= 223) {
            return event.updateSession.compute(this, operationName, ActionUpdateThread.EDT, supplier)
        }

        EDT.assertIsEdt()
        return supplier.get()
    }
}