package com.github.sagiri_kawaii01.blenh.base

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.github.sagiri_kawaii01.blenh.util.access.WechatAccess

class AccessibilityService: AccessibilityService() {

    private var t = 0L
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventTime - t < 300) {
            return
        }
        if (event.source == null) {
            return
        }
        t = event.eventTime
        Log.d("AccessibilityService", event.toString())
        val bill = when (event.packageName) {
            "com.tencent.mm" -> WechatAccess.handle(event.source!!)
            else -> null
        }
        if (null != bill) {
            Log.d("Bill", bill.money.toString())
            Log.d("Bill", bill.target!!)
            Log.d("Bill", bill.payMethod!!)
        }
    }

    override fun onInterrupt() {

    }





}