package com.github.sagiri_kawaii01.blenh.util.access

import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean

interface PayAccess {
    fun handle(root: AccessibilityNodeInfo): BillBean?

    fun findNodesById(root: AccessibilityNodeInfo, id: String): List<AccessibilityNodeInfo> {
        return root.findAccessibilityNodeInfosByViewId(id)
    }
}