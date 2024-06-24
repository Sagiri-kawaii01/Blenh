package com.github.sagiri_kawaii01.blenh.ui.component

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.github.sagiri_kawaii01.blenh.R
import com.github.sagiri_kawaii01.blenh.model.PayType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.ui.local.LocalNavController
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_EDIT_BILL
import com.github.sagiri_kawaii01.blenh.ui.route.ROUTE_PAY_DETAIL
import com.github.sagiri_kawaii01.blenh.ui.screen.billlist.BillRecord
import com.github.sagiri_kawaii01.blenh.ui.theme.Blue20
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20
import com.github.sagiri_kawaii01.blenh.ui.theme.Red60
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography
import com.github.sagiri_kawaii01.blenh.util.formatDate
import kotlinx.coroutines.launch

@Composable
fun DashConsume(
    label: String,
    billList: List<BillBean>,
    typeNameMap: Map<Int, String>,
    modifier: Modifier = Modifier
) {
    val navController = LocalNavController.current

    DashCard(label = label, modifier) {
        LazyColumn {
            items(billList) {
                ConsumeRecord(
                    type = PayType.fromId(it.payType),
                    amount = it.money,
                    label = typeNameMap[it.typeId] ?: "其他",
                    label2 = formatDate(it.month, it.day),
                    label3 = it.time,
                    onClick = {
                        navController.navigate("$ROUTE_PAY_DETAIL?id=${it.id}")
                    },
                    onEdit = {
                        navController.navigate("$ROUTE_EDIT_BILL?id=${it.id}")
                    },
                    onDelete = {

                    }
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
                        },
                        onEdit = {
                            navController.navigate("$ROUTE_EDIT_BILL?id=${item.id}")
                        },
                        onDelete = {

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
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
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

    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val maxOffset = DpOffset(150.dp, 0.dp)
    var showButton by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(40.dp)
            .background(Color.White)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (!showButton && offsetX.value < -maxOffset.x.toPx() / 4) {
                                offsetX.animateTo(-maxOffset.x.toPx(), animationSpec = tween(300))
                                showButton = true
                            } else if (showButton && offsetX.value > - 3 * maxOffset.x.toPx() / 4) {
                                offsetX.animateTo(0f, animationSpec = tween(300))
                                showButton = false
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            offsetX.snapTo((offsetX.value + dragAmount).coerceIn(-maxOffset.x.toPx(), 0f))
                        }
                    }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset { IntOffset(offsetX.value.toInt() + maxOffset.x.roundToPx(), 0) }
                .background(Color.Gray)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clickable { onEdit() }
                    .background(Blue20)
                    .width(60.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "编辑",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }

            Box(
                modifier = Modifier
                    .clickable { onDelete() }
                    .background(Red60)
                    .width(60.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "删除",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }


        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.toInt(), 0) }
                .background(Color.White)
                .clickable { onClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
                            color = Color.Gray
                        )
                        if (null != label3) {
                            Text(
                                text = label3,
                                style = Typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }

                }
            }
        }
    }
}