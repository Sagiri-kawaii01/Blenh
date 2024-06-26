package com.github.sagiri_kawaii01.blenh.model

enum class PayType(
    val id: Int,
    val nameZh: String
) {
    Wechat(1, "微信支付"),
    AliPay(2, "支付宝"),
    Rmb(3, "数字人民币")

    ;

    companion object {
        fun fromId(id: Int): PayType {
            return when (id) {
                1 -> Wechat
                2 -> AliPay
                3 -> Rmb
                else -> throw IllegalArgumentException("Unknown id: $id")
            }
        }
    }
}