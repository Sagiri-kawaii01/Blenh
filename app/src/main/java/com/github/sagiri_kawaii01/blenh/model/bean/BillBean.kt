package com.github.sagiri_kawaii01.blenh.model.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.sagiri_kawaii01.blenh.base.BaseBean
import com.github.sagiri_kawaii01.blenh.model.PayType
import kotlinx.serialization.Serializable

const val BILL_TABLE_NAME = "Bill"

@Serializable
@Entity(
    tableName = BILL_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = TypeBean::class,
            parentColumns = [TypeBean.ID_COLUMN],
            childColumns = [BillBean.TYPE_ID_COLUMN],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [BillBean.TYPE_ID_COLUMN], unique = false)
    ]
)
data class BillBean(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COLUMN)
    val id: Int = 0,
    @ColumnInfo(name = YEAR_COLUMN)
    var year: Int,
    @ColumnInfo(name = MONTH_COLUMN)
    var month: Int,
    @ColumnInfo(name = DAY_COLUMN)
    var day: Int,
    @ColumnInfo(name = TIME_COLUMN)
    var time: String,
    @ColumnInfo(name = TYPE_ID_COLUMN)
    var typeId: Int,
    @ColumnInfo(name = REMARK_COLUMN)
    var remark: String? = null,
    @ColumnInfo(name = MONEY_COLUMN)
    var money: Double,
    @ColumnInfo(name = ORDER_COLUMN)
    var order: String? = null,
    @ColumnInfo(name = PAY_TYPE_COLUMN)
    var payType: Int
): BaseBean {
    companion object {
        const val ID_COLUMN = "id"
        const val YEAR_COLUMN = "year"
        const val MONTH_COLUMN = "month"
        const val DAY_COLUMN = "day"
        const val TYPE_ID_COLUMN = "typeId"
        const val REMARK_COLUMN = "remark"
        const val MONEY_COLUMN = "money"
        const val ORDER_COLUMN = "order"
        const val PAY_TYPE_COLUMN = "payType"
        const val TIME_COLUMN = "time"

        val TEST_DATA = listOf(
            BillBean(
                year = 2024,
                month = 6,
                day = 7,
                typeId = 2,
                remark = "黄焖鸡米饭",
                money = 15.0,
                time = "13:58:00",
                payType = PayType.AliPay.id
            ),
            BillBean(
                year = 2024,
                month = 6,
                day = 7,
                typeId = 3,
                money = 12.52,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Wechat.id
            ),
            BillBean(
                year = 2024,
                month = 6,
                day = 7,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            ),
            BillBean(
                year = 2024,
                month = 6,
                day = 6,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            ),
            BillBean(
                year = 2024,
                month = 6,
                day = 5,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            ),
            BillBean(
                year = 2024,
                month = 6,
                day = 4,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            ),
            BillBean(
                year = 2024,
                month = 6,
                day = 3,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            ),
            BillBean(
                year = 2024,
                month = 5,
                day = 7,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            ),
            BillBean(
                year = 2024,
                month = 5,
                day = 17,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            ),
            BillBean(
                year = 2024,
                month = 5,
                day = 27,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            ),
            BillBean(
                year = 2023,
                month = 11,
                day = 7,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            ),
            BillBean(
                year = 2023,
                month = 12,
                day = 7,
                typeId = 5,
                money = 2.0,
                time = "17:23:13",
                order = "WX213123129312938129312",
                payType = PayType.Rmb.id
            )
        )
    }
}
