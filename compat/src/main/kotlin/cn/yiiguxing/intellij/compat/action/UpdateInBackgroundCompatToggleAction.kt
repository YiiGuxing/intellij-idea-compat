@file:Suppress("unused")

package cn.yiiguxing.intellij.compat.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.util.NlsActions
import org.jetbrains.annotations.ApiStatus
import java.util.function.Supplier
import javax.swing.Icon

abstract class UpdateInBackgroundCompatToggleAction : ToggleAction, UpdateInBackgroundCompat {

    constructor() : super()

    constructor(text: @NlsActions.ActionText String?) : super(text)

    constructor(text: Supplier<@NlsActions.ActionText String>) : super(text)

    constructor(
        text: @NlsActions.ActionText String?,
        description: @NlsActions.ActionDescription String?,
        icon: Icon?
    ) : super(text, description, icon)

    constructor(
        text: Supplier<@NlsActions.ActionText String>,
        description: Supplier<@NlsActions.ActionDescription String>,
        icon: Icon?
    ) : super(text, description, icon)

    constructor(text: Supplier<@NlsActions.ActionText String>, icon: Icon?) : super(text, icon)

    @ApiStatus.AvailableSince("222.3345.118")
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}