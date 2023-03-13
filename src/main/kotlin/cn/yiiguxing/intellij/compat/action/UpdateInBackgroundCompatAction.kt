@file:Suppress("unused", "UnstableApiUsage")

package cn.yiiguxing.intellij.compat.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.UpdateSession
import com.intellij.openapi.actionSystem.impl.Utils
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.util.BuildNumber
import com.intellij.openapi.util.NlsActions
import com.intellij.util.ui.EDT
import org.jetbrains.annotations.ApiStatus
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


    @ApiStatus.AvailableSince("222.3345.118")
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    @ApiStatus.AvailableSince("222.3345.118")
    protected fun <T> computeOnEDTUpdateSession(
        event: AnActionEvent,
        operationName: String,
        supplier: Supplier<out T>
    ): T {
        val build = ApplicationInfo.getInstance().build
        if (build >= BGT_SINCE_VERSION) {
            // Will not return null since 223.8617
            var updateSession: UpdateSession? = event.updateSession
            @Suppress("KotlinConstantConditions")
            if (updateSession == null) {
                @Suppress("DEPRECATION", "removal")
                updateSession = Utils.getOrCreateUpdateSession(event)
            }

            return updateSession.compute(this, operationName, ActionUpdateThread.EDT, supplier)
        }

        EDT.assertIsEdt()
        return supplier.get()
    }

    companion object {
        private val BGT_SINCE_VERSION = BuildNumber("", 222, 3345, 118)
    }
}