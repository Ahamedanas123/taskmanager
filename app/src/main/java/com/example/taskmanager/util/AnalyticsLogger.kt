package com.example.taskmanager.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

interface AnalyticsLogger {
    fun logEvent(eventName: String, params: Map<String, Any?> = emptyMap())
    fun logError(errorName: String, exception: Exception)
}

class AnalyticsLoggerImpl(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics
) : AnalyticsLogger {

    override fun logEvent(eventName: String, params: Map<String, Any?>) {
        analytics.logEvent(eventName, params.toBundle())
    }

    override fun logError(errorName: String, exception: Exception) {
        crashlytics.log(errorName)
        crashlytics.recordException(exception)
    }

    private fun Map<String, Any?>.toBundle(): Bundle {
        return Bundle().apply {
            forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Double -> putDouble(key, value)
                    is Float -> putFloat(key, value)
                    is Boolean -> putBoolean(key, value)
                    null -> putString(key, null)
                }
            }
        }
    }
}