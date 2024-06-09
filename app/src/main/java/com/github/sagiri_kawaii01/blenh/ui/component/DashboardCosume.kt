package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.sagiri_kawaii01.blenh.R
import com.github.sagiri_kawaii01.blenh.model.PayType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography

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
                    amount = it.money.toFloat(),
                    label = typeNameMap[it.typeId] ?: "其他"
                )
            }
        }
    }
}

@Composable
fun ConsumeRecord(
    type: PayType,
    amount: Float,
    label: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id =
            when (type) {
                PayType.Wechat -> R.drawable.wechat
                PayType.Rmb -> R.drawable.rmb
                PayType.AliPay -> R.drawable.alipay
            }
            ),
            contentDescription = "支付方式图标",
            modifier = Modifier
                .size(32.dp)
                .clip(shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                style = Typography.labelMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "￥$amount",
                style = Typography.labelSmall,
                color = Gray20
            )

        }
    }
}