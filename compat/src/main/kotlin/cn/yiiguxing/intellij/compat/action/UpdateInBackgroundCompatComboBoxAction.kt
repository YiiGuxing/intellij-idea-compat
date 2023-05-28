package cn.yiiguxing.intellij.compat.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.ex.ComboBoxAction
import org.jetbrains.annotations.ApiStatus

abstract class UpdateInBackgroundCompatComboBoxAction protected constructor() : ComboBoxAction(),
    UpdateInBackgroundCompat {

    @ApiStatus.AvailableSince("222.3345.118")
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

}