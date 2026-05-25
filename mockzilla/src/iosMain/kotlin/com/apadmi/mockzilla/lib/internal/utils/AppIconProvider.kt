package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.internal.PlatformConfig
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

internal actual fun fetchAppIconBytes(platformConfig: PlatformConfig): ByteArray? {
    val icons = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleIcons") as? Map<*, *>
    val primaryIcon = icons?.get("CFBundlePrimaryIcon") as? Map<*, *>
    val iconFiles = primaryIcon?.get("CFBundleIconFiles") as? List<*>
    val iconName = iconFiles?.lastOrNull()?.toString() ?: return null
    val image = UIImage.imageNamed(iconName) ?: return null
    return UIImagePNGRepresentation(image)?.toByteArray()
}

// Source: https://slack-chats.kotlinlang.org/t/9555639/hi-how-can-i-convert-a-swift-nsdata-to-kotlin-bytearray-back
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    return ByteArray(length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), bytes, length)
        }
    }
}