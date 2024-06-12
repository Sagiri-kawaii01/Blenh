package com.github.sagiri_kawaii01.blenh.util.access

import android.view.accessibility.AccessibilityNodeInfo
import com.github.sagiri_kawaii01.blenh.model.PayType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.util.format
import com.github.sagiri_kawaii01.blenh.util.now

object WechatAccess: PayAccess {
    private var bill: BillBean? = null
    override fun handle(root: AccessibilityNodeInfo): BillBean? {
        val kinda = findNodesById(root, "com.tencent.mm:id/kinda_main_container")
        if (kinda.isNotEmpty()) {
            val method = kinda.last().findAccessibilityNodeInfosByText("付款方式")
            if (method.isNotEmpty()) {
                val payInfoRoot = findWechatPayInfoRootNode(method[0])

                if (null != payInfoRoot) {
                    val now = now()
                    bill =  BillBean(
                        year = now.year,
                        month = now.monthValue,
                        day = now.dayOfMonth,
                        time = now.toLocalTime().format(),
                        typeId = 0,
                        money = payInfoRoot.getChild(1).getChild(0).text.toString().removePrefix("¥").removePrefix("￥").toDouble(),
                        payType = PayType.Wechat.id,
                        target = payInfoRoot.getChild(0).getChild(0).text.toString(),
                        payMethod = payInfoRoot.getChild(3).getChild(0).getChild(1).getChild(1).getChild(0).text.toString()
                    )
                }
            } else {
                val success = kinda[0].findAccessibilityNodeInfosByText("支付成功")
                // todo 跳出分类选择
                if (success.isNotEmpty()) {
                    val paySuccessRoot = findWechatPaySuccessRootNode(success[0])

                    if (null != paySuccessRoot) {
                        val now = now()
                        return bill?.copy(
                            year = now.year,
                            month = now.monthValue,
                            day = now.dayOfMonth,
                            time = now.toLocalTime().format(),
                            money = paySuccessRoot.getChild(2).getChild(0).text.toString().removePrefix("¥").removePrefix("￥").toDouble() ?: 0.0
                        )?.apply { bill = null }
                    }

                }
            }
        }
        return null
    }

    private fun findWechatPaySuccessRootNode(successTextNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        return when (successTextNode.className) {
            "android.widget.TextView" -> successTextNode.parent.parent.parent
            "android.view.ViewGroup" -> successTextNode.parent
            else -> successTextNode.parent.parent
        }
    }

    private fun findWechatPayInfoRootNode(payTypeNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        return payTypeNode.parent.parent
    }
}