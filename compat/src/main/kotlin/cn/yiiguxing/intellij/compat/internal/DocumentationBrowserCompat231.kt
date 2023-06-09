@file:Suppress("UnstableApiUsage")

package cn.yiiguxing.intellij.compat.internal

import cn.yiiguxing.intellij.compat.DocumentationBrowserCompat
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.platform.ide.documentation.DOCUMENTATION_BROWSER
import com.intellij.platform.ide.documentation.DocumentationBrowserFacade
import com.intellij.psi.PsiElement
import org.jetbrains.annotations.ApiStatus

@ApiStatus.AvailableSince("231")
internal class DocumentationBrowserCompat231 private constructor(private val facade: DocumentationBrowserFacade) :
    DocumentationBrowserCompat {

    override val targetElement: PsiElement?
        @Suppress("OverrideOnly")
        get() = facade.targetPointer.dereference()?.navigatable as? PsiElement

    override fun reload() {
        facade.reload()
    }

    @ApiStatus.AvailableSince("231")
    companion object {
        fun get(context: DataContext): DocumentationBrowserCompat? {
            return context.getData(DOCUMENTATION_BROWSER)
                ?.let { DocumentationBrowserCompat231(it) }
        }
    }
}