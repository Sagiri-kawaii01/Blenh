package com.github.sagiri_kawaii01.blenh.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.sagiri_kawaii01.blenh.ui.theme.Gray20
import com.github.sagiri_kawaii01.blenh.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearch: (String) -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var expand by remember { mutableStateOf(false) }
    val source = remember { MutableInteractionSource() }
    val width by animateDpAsState(targetValue = if (expand) 200.dp else 0.dp, label = "searchWidth")
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.CenterEnd
    ) {
        if (isSearchActive) {
            BasicTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                textStyle = Typography.bodyMedium,
                singleLine = true,
                modifier = Modifier
                    .width(width)
                    .focusRequester(focusRequester)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {}
                    .onFocusChanged {
                        if (expand && !it.hasFocus) {
                            expand = false
                            scope.launch {
                                delay(200)
                                isSearchActive = false
                            }
                            searchQuery = TextFieldValue("")
                        } else if (!expand && it.hasFocus) {
                            expand = true
                        }
                    }
                    .padding(0.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch(searchQuery.text)
                    isSearchActive = false
                    searchQuery = TextFieldValue("")
                }),
                interactionSource = source,
                decorationBox = @Composable { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = searchQuery.text,
                        visualTransformation = VisualTransformation.None,
                        innerTextField = innerTextField,
                        placeholder = null,
                        label = null,
                        leadingIcon = null,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Gray20
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        prefix = null,
                        suffix = null,
                        supportingText = null,
                        singleLine = true,
                        enabled = true,
                        isError = false,
                        interactionSource = source,
                        colors = OutlinedTextFieldDefaults.colors(),
                        container = {
                            OutlinedTextFieldDefaults.ContainerBox(
                                enabled = true,
                                isError = false,
                                source,
                                OutlinedTextFieldDefaults.colors(),
                                OutlinedTextFieldDefaults.shape
                            )
                        }
                    )
                }
            )
            LaunchedEffect(focusRequester) {
                focusRequester.requestFocus()
            }
        } else {
            IconButton(onClick = {
                isSearchActive = true
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Gray20
                )
            }
        }
    }
}