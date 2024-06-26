package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import java.time.LocalDate

@Composable
fun CalendarDialog(
    isDialogOpen: Boolean,
    initialSelection: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int) -> Unit
) {
    if (isDialogOpen) {
        val calendarState = rememberSelectableCalendarState(
            initialSelection = listOf(initialSelection)
        )

        AlertDialog(
            title = {
                Text(text = "选择日期")
            },
            text = {
                SelectableCalendar(
                    calendarState = calendarState
                )
            },
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(onClick = {
                    val selectDate = calendarState.selectionState.selection.first()
                    onConfirm(selectDate.year, selectDate.monthValue, selectDate.dayOfMonth)
                    onDismiss()
                }) {
                    Text(text = "确认")
                }
            }
        )
    }

}