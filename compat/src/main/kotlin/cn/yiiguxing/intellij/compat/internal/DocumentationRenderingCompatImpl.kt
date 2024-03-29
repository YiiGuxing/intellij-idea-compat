package cn.yiiguxing.intellij.compat.internal

import cn.yiiguxing.intellij.compat.DocumentationRenderingCompat
import com.intellij.codeInsight.documentation.render.DocRenderManager
import com.intellij.codeInsight.documentation.render.DocRenderPassFactory
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiFile
import com.intellij.util.concurrency.AppExecutorUtil
import org.jetbrains.concurrency.Promise

@Suppress("UnstableApiUsage")
internal class DocumentationRenderingCompatImpl : DocumentationRenderingCompat {
    override fun update(editor: Editor, psiFile: PsiFile): Promise<Boolean> {
        val project = psiFile.project
        return ReadAction
            .nonBlocking<DocRenderPassFactory.Items?> {
                try {
                    // if doc rendering is disabled, calculated items have textToRender=null
                    withDocRenderingEnabled(editor) {
                        DocRenderPassFactory.calculateItemsToRender(editor, psiFile)
                    }
                } catch (e: Throwable) {
                    LOG.warn("Failed to calculate doc items to render", e)
                    null
                }
            }
            .expireWhen { !editor.isValid }
            .withDocumentsCommitted(project)
            .finishOnUiThread(ModalityState.current()) { items ->
                if (items != null) {
                    DocRenderPassFactory.applyItemsToRender(editor, project, items, false)
                }
            }
            .submit(AppExecutorUtil.getAppExecutorService())
            .then { it != null }
    }

    /**
     * Items returned by [DocRenderPassFactory.calculateItemsToRender] contain text only if [DocRenderManager.isDocRenderingEnabled] is true.
     * [DocRenderManager.setDocRenderingEnabled] may be invoked only in UI thread.
     * Using [EditorSettingsExternalizable.setDocCommentRenderingEnabled] works in 2020.3, but in 2021.1 it updates some listeners and
     * requires UI thread as well. The only way I see to fix it now is via reflection.
     */
    @Suppress("KDocUnresolvedReference")
    private fun <T> withDocRenderingEnabled(editor: Editor, computation: () -> T): T {
        val key = docRenderEnabledKey() ?: return computation.invoke()
        val existing = editor.getUserData(key)
        editor.putUserData(key, true)
        try {
            return computation.invoke()
        } finally {
            editor.putUserData(key, existing)
        }
    }

    private fun docRenderEnabledKey(): Key<Boolean>? {
        return try {
            val docRenderManagerClass = DocRenderManager::class.java
            val field = docRenderManagerClass.getDeclaredField("DOC_RENDER_ENABLED")
            field.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val key = field.get(null) as? Key<Boolean>
            field.isAccessible = false
            key
        } catch (e: Exception) {
            null
        }
    }

    companion object {

        private val LOG = logger<DocumentationRenderingCompatImpl>()

        private val Editor.isValid: Boolean
            get() {
                val editorProject = project
                return editorProject != null
                        && !editorProject.isDisposed
                        && !isDisposed
                        && contentComponent.isShowing
            }
    }
}