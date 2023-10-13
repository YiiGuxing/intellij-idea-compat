package cn.yiiguxing.intellij.compat

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import org.jetbrains.concurrency.Promise

interface DocumentationRenderingCompat {

    fun update(editor: Editor, psiFile: PsiFile): Promise<Boolean>

    companion object
}