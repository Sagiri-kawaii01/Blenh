package com.github.sagiri_kawaii01.blenh.util.access

import android.view.accessibility.AccessibilityNodeInfo
import com.github.sagiri_kawaii01.blenh.model.PayType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.util.format
import com.github.sagiri_kawaii01.blenh.util.now

object AliPayAccess: PayAccess {
    override fun handle(root: AccessibilityNodeInfo): BillBean? {
        val payScaffold = root.findAccessibilityNodeInfosByViewId("com.alipay.android.app:id/flybird_layout")
        if (payScaffold.isNotEmpty() && payScaffold[0].childCount > 0) {
            val paySuccess = payScaffold[0].findAccessibilityNodeInfosByText("支付成功")
            val now = now()
            if (paySuccess.size == 2) {
                return BillBean(
                    year = now.year,
                    month = now.monthValue,
                    day = now.dayOfMonth,
                    time = now.toLocalTime().format(),
                    money = paySuccess[1].parent.parent.getChild(1).getChild(0).getChild(1).text.toString().toDouble(),
                    payType = PayType.AliPay.id,
                    typeId = 0,
                    payMethod = paySuccess[0].parent.getChild(1).getChild(1).getChild(1).getChild(0).getChild(0).text.toString(),
                    target = paySuccess[0].parent.getChild(1).getChild(0).getChild(0).text.toString()
                )
            }
        }
        return null
    }
}