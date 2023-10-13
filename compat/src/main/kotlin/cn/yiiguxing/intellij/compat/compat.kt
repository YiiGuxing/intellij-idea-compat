package cn.yiiguxing.intellij.compat

import cn.yiiguxing.intellij.compat.internal.DocumentationBrowserCompat213
import cn.yiiguxing.intellij.compat.internal.DocumentationBrowserCompat231
import cn.yiiguxing.intellij.compat.internal.DocumentationRenderingCompatImpl
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationInfo
import org.jetbrains.annotations.ApiStatus.AvailableSince

@AvailableSince("213")
fun DocumentationBrowserCompat.Companion.get(context: DataContext): DocumentationBrowserCompat? {
    val version = ApplicationInfo.getInstance().build.baselineVersion
    return when {
        version >= 231 -> DocumentationBrowserCompat231.get(context)
        version >= 213 -> DocumentationBrowserCompat213.get(context)
        else -> throw IllegalStateException("Unsupported IDE version: $version")
    }
}

fun DocumentationRenderingCompat.Companion.instance(): DocumentationRenderingCompat {
    return DocumentationRenderingCompatImpl()
}