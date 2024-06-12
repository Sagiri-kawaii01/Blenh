package com.github.sagiri_kawaii01.blenh.base

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AccessibilityService: AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d("AccessibilityService", event.toString())
        when (event.packageName) {
            "com.tencent.mm" -> listenWechat(event)
        }
    }

    override fun onInterrupt() {

    }

    private fun listenWechat(event: AccessibilityEvent) {
        val kinda = findNodesById(rootInActiveWindow, "com.tencent.mm:id/kinda_main_container")
        if (kinda.size == 3) {
            val method = kinda[2].findAccessibilityNodeInfosByText("付款方式")
            method
        }
    }

    private fun findNodesById(root: AccessibilityNodeInfo, id: String): List<AccessibilityNodeInfo> {
        return root.findAccessibilityNodeInfosByViewId(id)
    }
}