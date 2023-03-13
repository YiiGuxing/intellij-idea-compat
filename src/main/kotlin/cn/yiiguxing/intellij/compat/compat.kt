package cn.yiiguxing.intellij.compat

import cn.yiiguxing.intellij.compat.internal.DocumentationBrowserCompat213
import cn.yiiguxing.intellij.compat.internal.DocumentationBrowserCompat231
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationInfo

fun DocumentationBrowserCompat.Companion.get(context: DataContext): DocumentationBrowserCompat? {
    return if (ApplicationInfo.getInstance().build.baselineVersion >= 231) {
        DocumentationBrowserCompat231.get(context)
    } else {
        DocumentationBrowserCompat213.get(context)
    }
}