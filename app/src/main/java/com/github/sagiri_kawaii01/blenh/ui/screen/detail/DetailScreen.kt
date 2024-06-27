package com.github.sagiri_kawaii01.blenh.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.PayType
import com.github.sagiri_kawaii01.blenh.model.bean.IconBean
import com.github.sagiri_kawaii01.blenh.ui.component.WaitingDialog
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography
import com.github.sagiri_kawaii01.blenh.util.formatDate

@Composable
fun DetailScreen(
    billId: Int,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsState()
    viewModel.getDispatcher(startWith = DetailIntent.GetDetail(billId))

    WaitingDialog(visible = uiState.loadingDialog)
    
    when (uiState.detailDataState) {
        is DetailDataState.Success -> {
            val data = uiState.detailDataState as DetailDataState.Success
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = IconBean.IconList[data.icon.resId],
                    contentDescription = "icon",
                    modifier = Modifier.size(72.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(30.dp))

                val textData = listOf(
                    "金额" to "￥${data.bill.money.toString()}",
                    "支付日期" to "${data.bill.year}-${formatDate(data.bill.month, data.bill.day)}",
                    "支付时间" to data.bill.time,
                    "收款方" to data.bill.target,
                    "类型" to "${data.category.name}: ${data.type.name}",
                    "支付方式" to data.bill.payMethod,
                    "备注" to data.bill.remark,
                    "订单号" to data.bill.order,
                    "支付渠道" to PayType.fromId(data.bill.payType).nameZh
                )

                textData.filter { !it.second.isNullOrBlank() }.forEach {
                    DetailLine(it.first, it.second!!)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        is DetailDataState.Init -> {

        }
    }
}

@Composable
private fun DetailLine(
    title: String,
    content: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = title,
            modifier = Modifier.alignByBaseline()
        )
        Text(
            text = content,
            style = TextStyle.Default.copy(textDirection = TextDirection.Rtl ),
            modifier = Modifier.alignByBaseline()
        )
    }
}
