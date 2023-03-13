package cn.yiiguxing.intellij.compat

import com.intellij.psi.PsiElement

interface DocumentationBrowserCompat {

    val targetElement: PsiElement?
    fun reload()

    companion object
}