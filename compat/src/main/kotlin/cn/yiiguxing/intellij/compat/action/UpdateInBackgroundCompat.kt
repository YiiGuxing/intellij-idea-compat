@file:Suppress("unused", "UnstableApiUsage")

package cn.yiiguxing.intellij.compat.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.UpdateSession
import com.intellij.openapi.actionSystem.impl.Utils
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.util.BuildNumber
import com.intellij.util.ui.EDT
import org.jetbrains.annotations.ApiStatus
import java.util.function.Supplier

interface UpdateInBackgroundCompat {

    @ApiStatus.AvailableSince("222.3345.118")
    fun <T> computeOnEDTUpdateSession(
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
        val BGT_SINCE_VERSION = BuildNumber("", 222, 3345, 118)
    }

}