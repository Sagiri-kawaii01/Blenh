package com.github.sagiri_kawaii01.blenh.ui.screen.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.github.sagiri_kawaii01.blenh.model.PayType
import com.github.sagiri_kawaii01.blenh.model.bean.BillBean
import com.github.sagiri_kawaii01.blenh.ui.component.CalendarDialog
import com.github.sagiri_kawaii01.blenh.ui.component.DashCard
import com.github.sagiri_kawaii01.blenh.ui.component.DropdownOutlinedTextField
import com.github.sagiri_kawaii01.blenh.ui.component.EditTextField
import com.github.sagiri_kawaii01.blenh.ui.component.TimePickerDialog
import com.github.sagiri_kawaii01.blenh.ui.component.WaitingDialog
import com.github.sagiri_kawaii01.blenh.ui.local.LocalNavController
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20
import com.github.sagiri_kawaii01.blenh.util.formatDate
import com.github.sagiri_kawaii01.blenh.util.now
import com.github.sagiri_kawaii01.blenh.util.orNull
import com.github.sagiri_kawaii01.blenh.util.today
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun EditScreen(
    billId: Int? = null,
    viewModel: EditViewModel = hiltViewModel()
) {

    val navController = LocalNavController.current
    val uiState by viewModel.viewState.collectAsState()
    val today = today()
    val dispatcher = viewModel.getDispatcher(startWith = EditIntent.Init(billId))
    val uiEvent by viewModel.singleEvent.collectAsState(initial = null)
    var year by remember { mutableIntStateOf(today.year) }
    var month by remember { mutableIntStateOf(today.monthValue) }
    var day by remember { mutableIntStateOf(today.dayOfMonth) }
    var time by remember { mutableStateOf("${now().hour.toString().padStart(2, '0')}:${now().minute.toString().padStart(2, '0')}") }
    var money by remember { mutableDoubleStateOf(0.0) }
    var payType by remember { mutableIntStateOf(1) }
    var payMethod by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var order by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }
    var isDateDialogOpen by remember { mutableStateOf(false) }
    var isTimeDialogOpen by remember { mutableStateOf(false) }
    var selectedPayType by remember { mutableStateOf(PayType.Wechat.nameZh) }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var selectedTypeIndex by remember { mutableIntStateOf(0) }
    val payTypeEnum = PayType.entries.map { it.nameZh }

    if (uiEvent is EditEvent.SaveSuccess) {
        navController.popBackStack()
    }
    
    WaitingDialog(visible = uiState.loadingDialog)
    
    WaitingDialog(visible = uiState.savingDialog, title = "保存中")

    when (uiState.dataState) {
        is EditState.EditDataState.Loading -> {}
        is EditState.EditDataState.CategoryListSuccess -> {
            val dataState = uiState.dataState as EditState.EditDataState.CategoryListSuccess

            if (billId != null) {
                val bill = dataState.editBill!!
                year = bill.year
                month = bill.month
                day = bill.day
                time = bill.time
                money = bill.money
                payType = bill.payType
                payMethod = bill.payMethod ?: ""
                target = bill.target ?: ""
                order = bill.order ?: ""
                remark = bill.remark ?: ""
                selectedPayType = PayType.fromId(bill.payType).nameZh
                selectedCategoryIndex = dataState.categoryList.indexOfFirst { it.id == dataState.selectedCategoryId }
                selectedTypeIndex = dataState.typeList.indexOfFirst { it.id == dataState.selectedTypeId }
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

                    OutlinedTextField(
                        value = "$year-${formatDate(month, day)}",
                        label = { Text(text = "日期") },
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor,
                            disabledContainerColor = OutlinedTextFieldDefaults.colors().unfocusedContainerColor,
                            disabledTextColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor,
                            disabledLabelColor = OutlinedTextFieldDefaults.colors().unfocusedLabelColor,
                            disabledPlaceholderColor = OutlinedTextFieldDefaults.colors().unfocusedPlaceholderColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isDateDialogOpen = true
                            }
                    )

                    CalendarDialog(
                        isDialogOpen = isDateDialogOpen,
                        initialSelection = LocalDate.of(year, month, day),
                        onDismiss = { isDateDialogOpen = false }
                    ) { y, m, d ->
                        year = y
                        month = m
                        day = d
                    }

                    OutlinedTextField(
                        value = time,
                        label = { Text(text = "时间") },
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor,
                            disabledContainerColor = OutlinedTextFieldDefaults.colors().unfocusedContainerColor,
                            disabledTextColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor,
                            disabledLabelColor = OutlinedTextFieldDefaults.colors().unfocusedLabelColor,
                            disabledPlaceholderColor = OutlinedTextFieldDefaults.colors().unfocusedPlaceholderColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isTimeDialogOpen = true
                            }
                    )

                    TimePickerDialog(
                        isDialogOpen = isTimeDialogOpen,
                        initialSelection = time.split(":").let { LocalTime.of(it[0].toInt(), it[1].toInt()) },
                        onDismiss = {
                            isTimeDialogOpen = false
                        }) {
                        time = it
                    }

                    EditTextField(
                        value = money.toString(),
                        placeholder = "金额",
                        numberInput = true,
                        onValueChange = { money = it.toDouble() }
                    )

                    DropdownOutlinedTextField(
                        options = payTypeEnum,
                        label = "支付方式",
                        selectedOption = selectedPayType) {
                        selectedPayType = payTypeEnum[it]
                    }

                    DropdownOutlinedTextField(
                        options = dataState.categoryList.map { it.name },
                        label = "类别",
                        selectedOption = dataState.categoryList[selectedCategoryIndex].name
                    ) {
                        selectedCategoryIndex = it
                        dispatcher.invoke(EditIntent.GetTypes(dataState.categoryList[it].id))
                    }

                    DropdownOutlinedTextField(
                        options = dataState.typeList.map { it.name },
                        label = "类型",
                        selectedOption = dataState.typeList[selectedTypeIndex].name
                    ) {
                        selectedTypeIndex = it
                    }

                    EditTextField(
                        value = target,
                        placeholder = "收款方",
                        onValueChange = { target = it },
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
                                    typeId = dataState.typeList[selectedTypeIndex].id,
                                    time = time,
                                    money = money,
                                    payType = payType,
                                    payMethod = payMethod.orNull(),
                                    target = target.orNull(),
                                    remark = remark.orNull(),
                                    order = order.orNull()
                                )
                            ))
                        } else {
                            dispatcher(EditIntent.Update(
                                BillBean(
                                    id = billId,
                                    year = year,
                                    month = month,
                                    day = day,
                                    typeId = dataState.typeList[selectedTypeIndex].id,
                                    time = time,
                                    money = money,
                                    payType = payType,
                                    payMethod = payMethod.orNull(),
                                    target = target.orNull(),
                                    remark = remark.orNull(),
                                    order = order.orNull()
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
    }


}
