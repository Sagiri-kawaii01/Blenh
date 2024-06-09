package com.github.sagiri_kawaii01.blenh.model

enum class PayType(
    val id: Int
) {
    Wechat(1),
    AliPay(2),
    Rmb(3)

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