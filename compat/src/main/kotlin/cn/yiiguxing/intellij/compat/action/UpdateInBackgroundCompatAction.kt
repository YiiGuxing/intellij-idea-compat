@file:Suppress("unused")

package cn.yiiguxing.intellij.compat.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.util.NlsActions
import org.jetbrains.annotations.ApiStatus
import java.util.function.Supplier
import javax.swing.Icon

abstract class UpdateInBackgroundCompatAction : AnAction, UpdateInBackgroundCompat {

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
}