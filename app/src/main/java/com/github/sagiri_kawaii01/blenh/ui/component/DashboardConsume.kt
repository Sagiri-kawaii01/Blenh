package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.sagiri_kawaii01.blenh.R
import com.github.sagiri_kawaii01.blenh.model.PayType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.ui.local.LocalNavController
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_PAY_DETAIL
import com.github.sagiri_kawaii01.blenh.ui.screen.billlist.BillRecord
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography
import com.github.sagiri_kawaii01.blenh.util.formatDate

@Composable
fun DashConsume(
    label: String,
    billList: List<BillBean>,
    typeNameMap: Map<Int, String>,
    modifier: Modifier = Modifier
) {
    DashCard(label = label, modifier) {
        LazyColumn {
            items(billList) {
                ConsumeRecord(
                    type = PayType.fromId(it.payType),
                    amount = it.money,
                    label = typeNameMap[it.typeId] ?: "其他",
                    label2 = formatDate(it.month, it.day),
                    label3 = it.time
                )
            }
        }
    }
}

@Composable
fun BillList(
    label: String,
    billList: List<BillRecord>,
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current
    DashCard(label = label, modifier) {
        LazyColumn {
            items(billList) { item ->
                key(item.id) {
                    ConsumeRecord(
                        type = PayType.fromId(item.payType),
                        amount = item.money,
                        label = item.label,
                        label2 = item.date,
                        label3 = item.time,
                        clip = false,
                        onClick = {
                            navController.navigate("$ROUTE_PAY_DETAIL?id=${item.id}")
                        }
                    ) {
                        Icon(
                            imageVector = IconBean.IconList[item.iconIdx],
                            contentDescription = "",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConsumeRecord(
    type: PayType,
    amount: Double,
    label: String,
    label2: String? = null,
    label3: String? = null,
    clip: Boolean = true,
    onClick: (() -> Unit)? = null,
    icon: @Composable () -> Unit = {
        Image(
            painter = painterResource(id =
                when (type) {
                    PayType.Wechat -> R.drawable.wechat
                    PayType.Rmb -> R.drawable.rmb
                    PayType.AliPay -> R.drawable.alipay
                }
            ),
            contentDescription = "支付方式图标"
        )
    }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .run {
                if (onClick == null) {
                    this
                } else this.clickable {
                    onClick()
                }
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .run {
                    if (clip) {
                        this.clip(shape = CircleShape)
                    } else this
                }
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = Typography.labelMedium
                )
                if (null != label2) {
                    Text(
                        text = label2,
                        style = Typography.labelMedium
                    )

                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "￥$amount",
                    style = Typography.labelSmall,
                    color = Gray20
                )
                if (null != label3) {
                    Text(
                        text = label3,
                        style = Typography.labelSmall,
                        color = Gray20
                    )
                }
            }

        }
    }
}