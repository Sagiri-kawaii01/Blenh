package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.sagiri_kawaii01.blenh.base.mvi.getDispatcher
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.ui.component.DashCard
import com.github.sagiri_kawaii01.blenh.ui.local.LocalNavController
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20

@Composable
fun EditScreen(
    billId: Int? = null,
    viewModel: EditViewModel = hiltViewModel()
) {

    val navController = LocalNavController.current
    val uiState by viewModel.viewState.collectAsState()
    val dispatcher = viewModel.getDispatcher(startWith = EditIntent.Init)
    val uiEvent by viewModel.singleEvent.collectAsState(initial = null)
    var typeId by remember { mutableIntStateOf(0) }
    var year by remember { mutableIntStateOf(0) }
    var month by remember { mutableIntStateOf(0) }
    var day by remember { mutableIntStateOf(0) }
    var time by remember { mutableStateOf("") }
    var money by remember { mutableDoubleStateOf(0.0) }
    var payType by remember { mutableIntStateOf(1) }
    var payMethod by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var order by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }


    if (uiEvent is EditEvent.SaveSuccess) {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {

        DashCard(
            label = if (null == billId) {
                "添加账单"
            } else "编辑账单",
            modifier = Modifier.background(Color.White)
        ) {

            EditTextField(
                value = money.toString(),
                placeholder = "金额",
                onValueChange = { money = it.toDouble() }
            )

            EditTextField(
                value = payMethod,
                placeholder = "支付方式",
                onValueChange = { payMethod = it }
            )

            EditTextField(
                value = target,
                placeholder = "收款方",
                onValueChange = { target = it }
            )

            EditTextField(
                value = order,
                placeholder = "订单号",
                onValueChange = { order = it }
            )

            EditTextField(
                value = remark,
                placeholder = "备注",
                onValueChange = { remark = it }
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            onClick = {
                if (null == billId) {
                    dispatcher(EditIntent.Save(
                        BillBean(
                            year = year,
                            month = month,
                            day = day,
                            typeId = typeId,
                            time = time,
                            money = money,
                            payType = payType,
                            payMethod = payMethod,
                            target = target,
                            remark = remark,
                            order = order
                        )
                    ))
                } else {
                    dispatcher(EditIntent.Update(
                        BillBean(
                            id = billId,
                            year = year,
                            month = month,
                            day = day,
                            typeId = typeId,
                            time = time,
                            money = money,
                            payType = payType,
                            payMethod = payMethod,
                            target = target,
                            remark = remark,
                            order = order
                        )
                    ))
                }
            },
            colors = ButtonDefaults.outlinedButtonColors()
                .copy(
                    contentColor = Gray20,
                    containerColor = Color.White
                ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "保存")
        }
    }
}

@Composable
fun EditTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        label = { Text(text = placeholder) },
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Selector(
    items: List<String>,
    label: String,
    onSelect: (Int) -> Unit
) {

}