@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.lib.internal.persistance

/*
 * Adapted from: https://github.com/arkivanov/multiplatform-settings/blob/master/multiplatform-settings/src/appleMain/kotlin/com/russhwolf/settings/KeychainSettings.kt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import platform.CoreFoundation.CFArrayGetCount
import platform.CoreFoundation.CFArrayGetValueAtIndex
import platform.CoreFoundation.CFArrayRefVar
import platform.CoreFoundation.CFDictionaryCreate
import platform.CoreFoundation.CFDictionaryGetValue
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFBooleanFalse
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSKeyedArchiver
import platform.Foundation.NSKeyedUnarchiver
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Foundation.numberWithBool
import platform.Foundation.numberWithDouble
import platform.Foundation.numberWithFloat
import platform.Foundation.numberWithInt
import platform.Foundation.numberWithLongLong
import platform.Security.SecCopyErrorMessageString
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.SecItemUpdate
import platform.Security.errSecItemNotFound
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitAll
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnAttributes
import platform.Security.kSecReturnData
import platform.Security.kSecValueData
import platform.darwin.OSStatus

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.value

/**
 * A collection of storage-backed key-value data
 *
 * This class allows storage of values with the [Int], [Long], [String], [Float], [Double], or [Boolean] types, using a
 * [String] reference as a key. Values will be persisted across app launches.
 *
 * The specific persistence mechanism is defined using a platform-specific implementation, so certain behavior may vary
 * across platforms. In general, updates will be reflected immediately in-memory, but will be persisted to disk
 * asynchronously.
 *
 * Operator extensions are defined in order to simplify usage. In addition, property delegates are provided for cleaner
 * syntax and better type-safety when interacting with values stored in a `Settings` instance.
 *
 * The KeychainSettings implementation saves data to the Apple keychain. Data is saved using the generic password type,
 * where keys are account names and values are treated as passwords. The value passed to the `String` constructor will
 * be used as the service name. It's also possible to pass custom key-value pairs as attributes that will be added to
 * every key, if the default behavior does not fit your needs.
 */
class KeychainSettings(vararg defaultProperties: Pair<CFStringRef?, CFTypeRef?>) {
    @OptIn(ExperimentalForeignApi::class)
    private val defaultProperties = mapOf(kSecClass to kSecClassGenericPassword) + mapOf(*defaultProperties)

    @Suppress("NULLABLE_PROPERTY_TYPE")
    val keys: Set<String>
        get() = memScoped {
            val attributes = alloc<CFArrayRefVar>()
            val status = keyChainOperation(
                kSecMatchLimit to kSecMatchLimitAll,
                kSecReturnAttributes to kCFBooleanTrue
            ) { SecItemCopyMatching(it, attributes.ptr.reinterpret()) }
            status.checkError(errSecItemNotFound)
            if (status == errSecItemNotFound) {
                return emptySet()
            }

            // NB using this instead of List(count) { i -> ... } to avoid platform-dependent Int/Long conversion
            val count = CFArrayGetCount(attributes.value)
            val keys = mutableListOf<String>()
            for (i in 0 until count) {
                val item: CFDictionaryRef? = CFArrayGetValueAtIndex(attributes.value, i)?.reinterpret()
                val cfKey: CFStringRef? = CFDictionaryGetValue(item, kSecAttrAccount)?.reinterpret()
                val nsKey = CFBridgingRelease(cfKey) as NSString
                keys.add(nsKey.toKstring())
            }

            return keys.toSet()
        }

    val size: Int get() = keys.size
    // NB this calls CFBridgingRetain() without ever calling CFBridgingRelease()
    constructor(service: String) : this(kSecAttrService to CFBridgingRetain(service))

    fun clear(): Unit = keys.forEach { remove(it) }
    fun remove(key: String): Unit = removeKeychainItem(key)
    fun hasKey(key: String): Boolean = hasKeychainItem(key)

    fun putInt(key: String, value: Int): Unit =
        addOrUpdateKeychainItem(key, archiveNumber(NSNumber.numberWithInt(value)))

    fun getInt(key: String, defaultValue: Int): Int = getIntOrNull(key) ?: defaultValue
    fun getIntOrNull(key: String): Int? = unarchiveNumber(getKeychainItem(key))?.intValue

    fun putLong(key: String, value: Long): Unit =
        addOrUpdateKeychainItem(key, archiveNumber(NSNumber.numberWithLongLong(value)))

    fun getLong(key: String, defaultValue: Long): Long = getLongOrNull(key) ?: defaultValue
    fun getLongOrNull(key: String): Long? = unarchiveNumber(getKeychainItem(key))?.longLongValue

    fun putString(key: String, value: String): Unit =
        addOrUpdateKeychainItem(key, value.toNsstring().dataUsingEncoding(NSUTF8StringEncoding))

    fun getString(key: String, defaultValue: String): String = getStringOrNull(key) ?: defaultValue
    @OptIn(BetaInteropApi::class)
    fun getStringOrNull(key: String): String? =
        getKeychainItem(key)?.let { NSString.create(it, NSUTF8StringEncoding)?.toKstring() }

    fun putFloat(key: String, value: Float): Unit =
        addOrUpdateKeychainItem(key, archiveNumber(NSNumber.numberWithFloat(value)))

    fun getFloat(key: String, defaultValue: Float): Float = getFloatOrNull(key) ?: defaultValue
    fun getFloatOrNull(key: String): Float? = unarchiveNumber(getKeychainItem(key))?.floatValue

    fun putDouble(key: String, value: Double): Unit =
        addOrUpdateKeychainItem(key, archiveNumber(NSNumber.numberWithDouble(value)))

    fun getDouble(key: String, defaultValue: Double): Double = getDoubleOrNull(key) ?: defaultValue
    fun getDoubleOrNull(key: String): Double? = unarchiveNumber(getKeychainItem(key))?.doubleValue

    fun putBoolean(key: String, value: Boolean): Unit =
        addOrUpdateKeychainItem(key, archiveNumber(NSNumber.numberWithBool(value)))

    fun getBoolean(key: String, defaultValue: Boolean): Boolean = getBooleanOrNull(key) ?: defaultValue
    fun getBooleanOrNull(key: String): Boolean? = unarchiveNumber(getKeychainItem(key))?.boolValue

    private inline fun unarchiveNumber(data: NSData?): NSNumber? =
        data?.let { NSKeyedUnarchiver.unarchiveObjectWithData(it) } as? NSNumber

    private inline fun archiveNumber(number: NSNumber): NSData? =
        NSKeyedArchiver.archivedDataWithRootObject(number, true, null)

    private inline fun addOrUpdateKeychainItem(key: String, value: NSData?) {
        if (hasKeychainItem(key)) {
            updateKeychainItem(key, value)
        } else {
            addKeychainItem(key, value)
        }
    }

    private inline fun addKeychainItem(key: String, value: NSData?): Unit = cfRetain(key, value) { (cfKey, cfValue) ->
        val status = keyChainOperation(
            kSecAttrAccount to cfKey,
            kSecValueData to cfValue
        ) { SecItemAdd(it, null) }
        status.checkError()
    }

    private inline fun removeKeychainItem(key: String): Unit = cfRetain(key) { (cfKey) ->
        val status = keyChainOperation(
            kSecAttrAccount to cfKey,
        ) { SecItemDelete(it) }
        status.checkError(errSecItemNotFound)
    }

    private inline fun updateKeychainItem(key: String, value: NSData?): Unit =
        cfRetain(key, value) { (cfKey, cfValue) ->
            val status = keyChainOperation(
                kSecAttrAccount to cfKey,
                kSecReturnData to kCFBooleanFalse
            ) { SecItemUpdate(it, cfDictionaryOf(kSecValueData to cfValue)) }
            status.checkError()
        }

    private inline fun getKeychainItem(key: String): NSData? = cfRetain(key) { (cfKey) ->
        val cfValue = alloc<CFTypeRefVar>()
        val status = keyChainOperation(
            kSecAttrAccount to cfKey,
            kSecReturnData to kCFBooleanTrue,
            kSecMatchLimit to kSecMatchLimitOne
        ) { SecItemCopyMatching(it, cfValue.ptr) }
        status.checkError(errSecItemNotFound)
        if (status == errSecItemNotFound) {
            return@cfRetain null
        }
        CFBridgingRelease(cfValue.value) as? NSData
    }

    private inline fun hasKeychainItem(key: String): Boolean = cfRetain(key) { (cfKey) ->
        val status = keyChainOperation(
            kSecAttrAccount to cfKey,
            kSecMatchLimit to kSecMatchLimitOne
        ) { SecItemCopyMatching(it, null) }

        status != errSecItemNotFound
    }

    private inline fun MemScope.keyChainOperation(
        vararg input: Pair<CFStringRef?, CFTypeRef?>,
        operation: (query: CFDictionaryRef?) -> OSStatus,
    ): OSStatus {
        val query = cfDictionaryOf(defaultProperties + mapOf(*input))
        return operation(query)
    }

    private inline fun OSStatus.checkError(vararg expectedErrors: OSStatus) {
        if (this != 0 && this !in expectedErrors) {
            val cfMessage = SecCopyErrorMessageString(this, null)
            val nsMessage = CFBridgingRelease(cfMessage) as? NSString
            val message = nsMessage?.toKstring() ?: "Unknown error"
            error("Keychain error $this: $message")
        }
    }
}

internal inline fun MemScope.cfDictionaryOf(vararg items: Pair<CFStringRef?, CFTypeRef?>): CFDictionaryRef? =
    cfDictionaryOf(mapOf(*items))

internal inline fun MemScope.cfDictionaryOf(map: Map<CFStringRef?, CFTypeRef?>): CFDictionaryRef? {
    val size = map.size
    val keys = allocArrayOf(*map.keys.toTypedArray())
    val values = allocArrayOf(*map.values.toTypedArray())
    return CFDictionaryCreate(
        kCFAllocatorDefault,
        keys.reinterpret(),
        values.reinterpret(),
        size.convert(),
        null,
        null
    )
}

// Turn casts into dot calls for better readability
@Suppress("CAST_NEVER_SUCCEEDS")
internal inline fun String.toNsstring() = this as NSString

@Suppress("CAST_NEVER_SUCCEEDS")
internal inline fun NSString.toKstring() = this as String

internal inline fun <T> cfRetain(vararg values: Any?, block: MemScope.(Array<CFTypeRef?>) -> T): T = memScoped {
    val cfValues = Array(values.size) { i -> CFBridgingRetain(values[i]) }
    return try {
        block(cfValues)
    } finally {
        cfValues.forEach { CFBridgingRelease(it) }
    }
}
