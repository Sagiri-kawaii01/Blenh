package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography

@Composable
fun EditTextField(
    value: String,
    placeholder: String,
    numberInput: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        label = { Text(text = placeholder) },
        onValueChange = {
            if (numberInput) {
                if (it.matches(Regex("^\\d*\\.?\\d*"))) {
                    onValueChange(it)
                } else if (it.isBlank()) {
                    onValueChange("0")
                }
            } else {
                onValueChange(it)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = if (numberInput) {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        } else {
            KeyboardOptions.Default
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownOutlinedTextField(
    options: List<String>,
    label: String,
    selectedOption: String,
    onOptionSelected: (Int) -> Unit
) {
    var expand by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    ExposedDropdownMenuBox(
        expanded = expand,
        onExpandedChange = { expand = !expand }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(text = label) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .focusRequester(focusRequester),
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = if (expand) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor,
                disabledContainerColor = OutlinedTextFieldDefaults.colors().unfocusedContainerColor,
                disabledTextColor = OutlinedTextFieldDefaults.colors().unfocusedTextColor,
                disabledLabelColor = OutlinedTextFieldDefaults.colors().unfocusedLabelColor,
                disabledPlaceholderColor = OutlinedTextFieldDefaults.colors().unfocusedPlaceholderColor
            ),
        )

        ExposedDropdownMenu(
            expanded = expand,
            onDismissRequest = { expand = false },
            modifier = Modifier.fillMaxWidth().background(Color.White)
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(index)
                        expand = false
                    },
                    text = {
                        Text(
                            text = option,
                            style = Typography.bodyMedium
                        )
                    },
                    modifier = Modifier.background(Color.White)
                )
            }
        }
    }
}