package com.github.sagiri_kawaii01.blenh.base

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class AccessibilityService: AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d("AccessibilityService", event.toString())
    }

    override fun onInterrupt() {

    }
}